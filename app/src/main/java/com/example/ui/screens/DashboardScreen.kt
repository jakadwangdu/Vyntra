package com.example.ui.screens

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
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
