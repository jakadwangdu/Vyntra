package com.example.data.model

data class FoodScanResult(
    val name: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val portionGrams: Int,
    val description: String,
    val ingredients: List<String>,
    val micronutrients: String,
    val imageUrl: String = "",
    val dietaryTag: String = ""
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: MessageSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

enum class MessageSender {
    USER,
    AI_COACH
}

data class Recipe(
    val id: String,
    val title: String,
    val category: String, // All, Vegan, Protein, Snacks, Low Carb
    val timeMinutes: Int,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val difficulty: String,
    val imageUrl: String,
    val description: String,
    val ingredients: List<String>
)

data class Exercise(
    val id: String,
    val name: String,
    val targetMuscle: String,
    val sets: Int,
    val reps: String,
    val restSeconds: Int,
    val caloriesBurnEstimate: Int,
    val formInstructions: List<String>,
    val tips: String,
    val videoPreviewUrl: String = ""
)

data class WorkoutRoutine(
    val id: String,
    val title: String,
    val goalType: String, // "BULK" or "CUT"
    val durationMinutes: Int,
    val level: String,
    val description: String,
    val estimatedCalories: Int,
    val exercises: List<Exercise>
)
