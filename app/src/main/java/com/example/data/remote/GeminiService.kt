package com.example.data.remote

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.example.BuildConfig
import com.example.data.model.FoodScanResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

class GeminiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    private fun getApiKey(): String {
        return try {
            val key = BuildConfig.GEMINI_API_KEY
            if (key.isBlank() || key == "YOUR_GEMINI_API_KEY" || key == "DEFAULT_KEY" || key == "DEFAULT_GEMINI_API_KEY" || key.contains("PLACEHOLDER", ignoreCase = true)) {
                ""
            } else {
                key
            }
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun analyzeFoodImage(bitmap: Bitmap): Result<FoodScanResult> = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isBlank()) {
            return@withContext Result.failure(Exception("Gemini API key is not configured."))
        }

        try {
            // Compress bitmap to base64 jpeg
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            val base64Image = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)

            val prompt = """
                You are Vyntra AI, an expert clinical nutritionist and computer vision nutrition analyzer.
                Examine this food photo carefully.
                Identify the primary dish/food and accurately calculate its macronutrients and key micronutrients.
                Return strictly valid JSON with the following schema, NO markdown, NO backticks:
                {
                  "name": "Food Name",
                  "calories": 350,
                  "protein": 24,
                  "carbs": 38,
                  "fat": 12,
                  "portionGrams": 220,
                  "description": "Short appetizing description and nutritional highlight (max 2 sentences)",
                  "ingredients": ["Ingredient 1", "Ingredient 2", "Ingredient 3"],
                  "micronutrients": "e.g. Calcium: 12% DV, Iron: 8% DV, Potassium: 340mg"
                }
            """.trimIndent()

            // Build request payload
            val rootJson = JSONObject().apply {
                val contents = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val parts = JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                            put(JSONObject().apply {
                                put("inlineData", JSONObject().apply {
                                    put("mimeType", "image/jpeg")
                                    put("data", base64Image)
                                })
                            })
                        }
                        put("parts", parts)
                    }
                    put(contentObj)
                }
                put("contents", contents)

                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.2)
                    put("topP", 0.8)
                    put("responseMimeType", "application/json")
                })
            }

            val requestBody = rootJson.toString().toRequestBody(jsonMediaType)
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                val errBody = response.body?.string() ?: "Unknown error"
                Log.e("GeminiService", "Vision API error: ${response.code} $errBody")
                return@withContext Result.failure(Exception("API returned code ${response.code}: $errBody"))
            }

            val responseStr = response.body?.string() ?: ""
            val jsonResponse = JSONObject(responseStr)
            val candidates = jsonResponse.optJSONArray("candidates")
            val candidate = candidates?.optJSONObject(0)
            val parts = candidate?.optJSONObject("content")?.optJSONArray("parts")
            val text = parts?.optJSONObject(0)?.optString("text") ?: ""

            // Clean markdown code blocks if any
            val cleanJson = text.replace("```json", "").replace("```", "").trim()
            val parsed = JSONObject(cleanJson)

            val ingredientsList = mutableListOf<String>()
            val ingredientsArray = parsed.optJSONArray("ingredients")
            if (ingredientsArray != null) {
                for (i in 0 until ingredientsArray.length()) {
                    ingredientsList.add(ingredientsArray.getString(i))
                }
            } else {
                ingredientsList.addAll(listOf("Natural ingredients", "Protein blend", "Healthy seasoning"))
            }

            val result = FoodScanResult(
                name = parsed.optString("name", "Identified Dish"),
                calories = parsed.optInt("calories", 250),
                protein = parsed.optInt("protein", 15),
                carbs = parsed.optInt("carbs", 30),
                fat = parsed.optInt("fat", 8),
                portionGrams = parsed.optInt("portionGrams", 150),
                description = parsed.optString("description", "A balanced nutritious dish identified by Vyntra AI."),
                ingredients = ingredientsList,
                micronutrients = parsed.optString("micronutrients", "Calcium: 10% DV, Iron: 6% DV, Potassium: 280mg")
            )

            Result.success(result)
        } catch (e: Exception) {
            Log.e("GeminiService", "Failed to scan food", e)
            Result.failure(e)
        }
    }

    suspend fun chatWithCoach(
        userMessage: String,
        userGoal: String,
        caloriesRemaining: Int,
        loggedMealsSummary: String,
        history: List<Pair<String, String>>
    ): String = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isBlank()) {
            return@withContext fallbackCoachResponse(userMessage, userGoal)
        }

        try {
            val systemInstruction = """
                You are Vyntra AI Coach, an expert fitness trainer, sports dietitian, and personal coach.
                Your communication style is minimalist, brutalist, functional, and highly practical.
                Current User Context:
                - Fitness Goal: $userGoal
                - Calories remaining today: $caloriesRemaining kcal
                - Meals logged today: $loggedMealsSummary
                
                Keep answers concise, direct, evidence-based, and immediately actionable. Avoid fluff or corporate cheerleading.
            """.trimIndent()

            val contentsArray = JSONArray()

            // System prompt & user context as first turn
            contentsArray.put(JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().apply {
                    put(JSONObject().apply { put("text", "System setup: $systemInstruction") })
                })
            })
            contentsArray.put(JSONObject().apply {
                put("role", "model")
                put("parts", JSONArray().apply {
                    put(JSONObject().apply { put("text", "Understood. Vyntra AI Coach initialized.") })
                })
            })

            // Append last 4 history turns
            val recentHistory = history.takeLast(4)
            for (turn in recentHistory) {
                contentsArray.put(JSONObject().apply {
                    put("role", if (turn.first == "USER") "user" else "model")
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply { put("text", turn.second) })
                    })
                })
            }

            // Current message
            contentsArray.put(JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().apply {
                    put(JSONObject().apply { put("text", userMessage) })
                })
            })

            val rootJson = JSONObject().apply {
                put("contents", contentsArray)
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.5)
                    put("maxOutputTokens", 500)
                })
            }

            val requestBody = rootJson.toString().toRequestBody(jsonMediaType)
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext fallbackCoachResponse(userMessage, userGoal)
            }

            val responseStr = response.body?.string() ?: ""
            val jsonResponse = JSONObject(responseStr)
            val text = jsonResponse.optJSONArray("candidates")
                ?.optJSONObject(0)
                ?.optJSONObject("content")
                ?.optJSONArray("parts")
                ?.optJSONObject(0)
                ?.optString("text")

            text?.trim() ?: fallbackCoachResponse(userMessage, userGoal)
        } catch (e: Exception) {
            Log.e("GeminiService", "Chat error", e)
            fallbackCoachResponse(userMessage, userGoal)
        }
    }

    private fun fallbackCoachResponse(prompt: String, goal: String): String {
        val p = prompt.lowercase()
        return when {
            "high-protein" in p || "protein" in p || "snack" in p -> {
                "High-Protein Snacks (${if (goal == "BULK") "Surplus" else "Deficit"}):\n• Edamame (1 cup): 17g protein, 188 kcal\n• Greek Yogurt (0% fat, 200g): 20g protein, 120 kcal\n• Roasted Chickpeas (100g): 19g protein\n• Cottage Cheese with pumpkin seeds: 24g protein"
            }
            "deadlift" in p || "form" in p -> {
                "Deadlift Form Essentials:\n1. Bar directly over mid-foot.\n2. Shins touching the bar; hips higher than knees.\n3. Neutral spine, lats engaged (squeeze armpits).\n4. Push the floor away through mid-foot; lock out hips without hyperextending lower back."
            }
            "cutting" in p || "fat burn" in p || "deficit" in p -> {
                "For a clean Cut:\n• Maintain a 400-500 kcal deficit below maintenance.\n• Keep protein high (2.0g - 2.2g per kg bodyweight) to preserve lean muscle mass.\n• Prioritize fibrous leafy greens and water intake to stay satiated."
            }
            "bulking" in p || "bulk" in p -> {
                "For a clean Bulk:\n• Aim for a 250-400 kcal lean surplus.\n• Protein: 1.8g - 2.0g per kg.\n• Focus on progressive overload in compound lifts (squats, bench, deadlifts, overhead presses)."
            }
            else -> {
                "Vyntra Coach: Stay consistent with your macro targets for $goal. Prioritize nutrient-dense whole foods, progressive resistance training, and hit 64 fl oz of water daily."
            }
        }
    }
}
