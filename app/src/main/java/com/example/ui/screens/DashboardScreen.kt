package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.local.MealEntity
import com.example.ui.components.CalorieSummaryCard
import com.example.ui.theme.NutriBg
import com.example.ui.theme.NutriBlack
import com.example.ui.theme.NutriBorder
import com.example.ui.theme.NutriBurnRed
import com.example.ui.theme.NutriDarkGray
import com.example.ui.theme.NutriGray
import com.example.ui.theme.NutriGreenAccent
import com.example.ui.theme.NutriWaterBlue
import com.example.ui.theme.NutriWhite
import com.example.ui.viewmodel.NutriLensViewModel
import com.example.ui.viewmodel.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: NutriLensViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val weekDays by viewModel.weekDays.collectAsStateWithLifecycle()
    val meals by viewModel.currentMeals.collectAsStateWithLifecycle()
    val waterLog by viewModel.currentWater.collectAsStateWithLifecycle()
    val workouts by viewModel.currentWorkouts.collectAsStateWithLifecycle()

    val targetCalories = profile?.dailyCalorieTarget ?: 2400
    val targetProtein = profile?.dailyProteinTarget ?: 160
    val targetCarbs = profile?.dailyCarbsTarget ?: 220
    val targetFat = profile?.dailyFatTarget ?: 65

    val eatenCalories = meals.sumOf { it.calories }
    val burnedCalories = workouts.sumOf { it.caloriesBurned }
    val caloriesLeft = (targetCalories - eatenCalories + burnedCalories).coerceAtLeast(0)

    val currentProtein = meals.sumOf { it.protein }
    val currentCarbs = meals.sumOf { it.carbs }
    val currentFat = meals.sumOf { it.fat }

    var mealToDelete by remember { mutableStateOf<MealEntity?>(null) }
    var foodSearchQuery by remember { mutableStateOf("") }
    val isAiFoodSearching by viewModel.isAiFoodSearching.collectAsStateWithLifecycle()

    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    if (mealToDelete != null) {
        AlertDialog(
            onDismissRequest = { mealToDelete = null },
            title = { Text("Delete Meal", fontWeight = FontWeight.Bold) },
            text = { Text("Remove '${mealToDelete?.name}' (${mealToDelete?.calories} kcal)?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        mealToDelete?.let { viewModel.deleteMeal(it.id) }
                        mealToDelete = null
                    }
                ) {
                    Text("Delete", color = NutriBurnRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mealToDelete = null }) {
                    Text("Cancel", color = NutriBlack)
                }
            },
            containerColor = NutriWhite
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NutriBg),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar: Greeting & AI Coach chat icon
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Good morning",
                        style = MaterialTheme.typography.bodyMedium,
                        color = NutriGray
                    )
                    Text(
                        text = profile?.name ?: "Yohanan Rashad",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        ),
                        color = NutriBlack
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // GitHub Download APK button
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(NutriWhite)
                            .border(1.dp, NutriBorder, CircleShape)
                            .clickable { 
                                try {
                                    uriHandler.openUri("https://github.com/skituspanda/Vyntra/releases/latest/download/Vyntra.apk")
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Could not open link", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .testTag("dashboard_download_apk_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CloudDownload,
                            contentDescription = "Download APK via GitHub",
                            tint = NutriBlack,
                            modifier = Modifier.size(20.dp)
                        )
                        // Green accent dot
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 8.dp, end = 8.dp)
                                .size(7.dp)
                                .background(NutriGreenAccent, CircleShape)
                        )
                    }

                    // AI Coach shortcut button with active status dot
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(NutriWhite)
                            .border(1.dp, NutriBorder, CircleShape)
                            .clickable { viewModel.navigateTo(Screen.Chatbot) }
                            .testTag("dashboard_chat_icon"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ChatBubbleOutline,
                            contentDescription = "Chat with AI Coach",
                            tint = NutriBlack,
                            modifier = Modifier.size(20.dp)
                        )
                        // Red notification dot (matching mockup)
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 8.dp, end = 8.dp)
                                .size(7.dp)
                                .background(NutriBurnRed, CircleShape)
                        )
                    }
                }
            }
        }

        // Ask Gemini Food Search Bar & AI Analyzer Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, NutriBorder, RoundedCornerShape(16.dp)),
                color = NutriWhite,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = NutriGreenAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Search Food with Gemini AI",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = NutriBlack
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "FLASH 3.5",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp,
                                    color = NutriGreenAccent
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = foodSearchQuery,
                        onValueChange = { foodSearchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dashboard_gemini_food_input"),
                        placeholder = {
                            Text(
                                text = "Enter any food name (e.g. Biryani, Salmon Bowl)...",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                                color = NutriGray
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search",
                                tint = NutriBlack,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            if (isAiFoodSearching) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = NutriGreenAccent
                                )
                            } else if (foodSearchQuery.isNotBlank()) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(NutriBlack)
                                        .clickable {
                                            viewModel.searchAndAnalyzeFoodName(foodSearchQuery)
                                        }
                                        .testTag("dashboard_gemini_search_submit"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowForward,
                                        contentDescription = "Analyze Food",
                                        tint = NutriWhite,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = NutriBlack,
                            unfocusedTextColor = NutriBlack,
                            focusedContainerColor = Color(0xFFFBFBFA),
                            unfocusedContainerColor = Color(0xFFFBFBFA),
                            focusedBorderColor = NutriGreenAccent,
                            unfocusedBorderColor = NutriBorder
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                if (foodSearchQuery.isNotBlank()) {
                                    viewModel.searchAndAnalyzeFoodName(foodSearchQuery)
                                }
                            }
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Quick AI Search Chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            "🍛 Paneer Butter Masala",
                            "🥑 Avocado Toast",
                            "🍜 Tonkotsu Ramen",
                            "🥗 Quinoa Greek Salad",
                            "🥤 Whey Protein Shake",
                            "🌮 Chicken Fajitas"
                        ).forEach { tag ->
                            val cleanName = tag.substringAfter(" ")
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF3F3F3))
                                    .clickable {
                                        foodSearchQuery = cleanName
                                        viewModel.searchAndAnalyzeFoodName(cleanName)
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = tag,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 11.sp
                                    ),
                                    color = NutriBlack
                                )
                            }
                        }
                    }
                }
            }
        }

        // Custom AI Diet Plan Banner
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, NutriBorder, RoundedCornerShape(14.dp))
                    .clickable { viewModel.navigateTo(Screen.DietPlan) }
                    .testTag("dashboard_diet_plan_banner"),
                color = NutriBlack,
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(NutriGreenAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Restaurant,
                                contentDescription = null,
                                tint = NutriBlack,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Custom AI Diet Plan",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                ),
                                color = NutriWhite
                            )
                            Text(
                                text = "AI-calibrated daily nutrition & macros",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = NutriGray
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NutriGreenAccent)
                            .padding(horizontal = 9.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "VIEW",
                            color = NutriWhite,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }

        // Horizontal Date Selector (matches mockup: Mon 08, Tue 09, Thu 11 in green pill, etc.)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                weekDays.forEach { day ->
                    val isSelected = day.isSelected
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) NutriGreenAccent else NutriWhite)
                            .border(1.dp, if (isSelected) NutriGreenAccent else NutriBorder, RoundedCornerShape(20.dp))
                            .clickable { viewModel.selectDate(day.dateString) }
                            .padding(vertical = 12.dp, horizontal = 14.dp)
                            .testTag("date_pill_${day.dayOfMonth}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = day.dayOfWeek,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) NutriWhite.copy(alpha = 0.8f) else NutriGray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = day.dayOfMonth,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                ),
                                color = if (isSelected) NutriWhite else NutriBlack
                            )
                        }
                    }
                }
            }
        }

        // Calorie & Macro Card (matching mockup)
        item {
            CalorieSummaryCard(
                caloriesLeft = caloriesLeft,
                caloriesEaten = eatenCalories,
                caloriesBurned = burnedCalories,
                targetCalories = targetCalories,
                carbsGrams = currentCarbs,
                targetCarbs = targetCarbs,
                proteinGrams = currentProtein,
                targetProtein = targetProtein,
                fatGrams = currentFat,
                targetFat = targetFat
            )
        }

        // Meals Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Meals",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = NutriBlack
                )
            }
        }

        // Meal Blocks: Breakfast, Lunch, Dinner, Snacks
        val mealCategories = listOf(
            Triple("Breakfast", 560, meals.filter { it.mealType.equals("Breakfast", ignoreCase = true) }),
            Triple("Lunch", 800, meals.filter { it.mealType.equals("Lunch", ignoreCase = true) }),
            Triple("Dinner", 700, meals.filter { it.mealType.equals("Dinner", ignoreCase = true) }),
            Triple("Snacks", 340, meals.filter { it.mealType.equals("Snacks", ignoreCase = true) || it.mealType.equals("Snack", ignoreCase = true) })
        )

        items(mealCategories.size) { index ->
            val (mealName, budget, loggedList) = mealCategories[index]
            val totalInMeal = loggedList.sumOf { it.calories }

            MealCategoryCard(
                mealName = mealName,
                caloriesEaten = totalInMeal,
                caloriesBudget = budget,
                loggedItems = loggedList,
                onAddClick = {
                    viewModel.navigateTo(Screen.Scanner)
                },
                onItemClick = { item ->
                    mealToDelete = item
                }
            )
        }

        // Water Section (matching mockup bottom)
        item {
            val totalGlasses = waterLog?.glasses ?: 0
            val flOz = waterLog?.flOz ?: 0
            val goalFlOz = profile?.dailyWaterTargetFlOz ?: 64

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(NutriWhite)
                    .border(1.dp, NutriBorder, RoundedCornerShape(24.dp))
                    .padding(18.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Water",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = NutriBlack
                        )
                        Text(
                            text = "$flOz fl oz | Goal: $goalFlOz fl oz",
                            style = MaterialTheme.typography.bodyMedium,
                            color = NutriGray
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Row of Water Cups
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Quick Add Button
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFE0F2FE))
                                .clickable { viewModel.addWaterGlass() }
                                .testTag("water_add_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add Water Glass",
                                tint = NutriWaterBlue,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        // Display 8 glass slots
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            for (i in 1..8) {
                                val isFilled = i <= totalGlasses
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isFilled) NutriWaterBlue.copy(alpha = 0.85f)
                                            else Color(0xFFF1F5F9)
                                        )
                                        .clickable {
                                            if (isFilled && i == totalGlasses) {
                                                viewModel.removeWaterGlass()
                                            } else if (!isFilled && i == totalGlasses + 1) {
                                                viewModel.addWaterGlass()
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.LocalDrink,
                                        contentDescription = "Glass $i",
                                        tint = if (isFilled) NutriWhite else Color(0xFFCBD5E1),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MealCategoryCard(
    mealName: String,
    caloriesEaten: Int,
    caloriesBudget: Int,
    loggedItems: List<MealEntity>,
    onAddClick: () -> Unit,
    onItemClick: (MealEntity) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NutriWhite)
            .border(1.dp, NutriBorder, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = mealName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = NutriGreenAccent
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$caloriesEaten cal / $caloriesBudget cal",
                        style = MaterialTheme.typography.bodyMedium,
                        color = NutriDarkGray
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Preview thumbnails of logged food items
                    loggedItems.take(3).forEach { item ->
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEAEAEA))
                                .border(1.dp, NutriBorder, CircleShape)
                                .clickable { onItemClick(item) },
                            contentAlignment = Alignment.Center
                        ) {
                            if (item.imageUrl.isNotBlank()) {
                                AsyncImage(
                                    model = item.imageUrl,
                                    contentDescription = item.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Text(
                                    text = item.name.take(1).uppercase(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = NutriBlack
                                )
                            }
                        }
                    }

                    // Add Meal button (+)
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8EFE6))
                            .clickable { onAddClick() }
                            .testTag("add_${mealName.lowercase()}_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add $mealName",
                            tint = NutriGreenAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // If items logged, list their names with delete option
            if (loggedItems.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    loggedItems.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFAFAFA))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${item.name} (${item.calories} kcal)",
                                style = MaterialTheme.typography.bodySmall,
                                color = NutriDarkGray
                            )
                            IconButton(
                                onClick = { onItemClick(item) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.DeleteOutline,
                                    contentDescription = "Delete item",
                                    tint = NutriGray,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
