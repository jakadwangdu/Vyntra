package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.ui.theme.NutriBlack
import com.example.ui.theme.NutriBorder
import com.example.ui.theme.NutriCarbsAmber
import com.example.ui.theme.NutriDarkGray
import com.example.ui.theme.NutriFatCoral
import com.example.ui.theme.NutriFlameOrange
import com.example.ui.theme.NutriGray
import com.example.ui.theme.NutriGreenAccent
import com.example.ui.theme.NutriProteinGreen
import com.example.ui.theme.NutriWhite
import com.example.ui.viewmodel.NutriLensViewModel
import com.example.ui.viewmodel.Screen
import kotlinx.coroutines.launch

@Composable
fun FoodDetailScreen(
    viewModel: NutriLensViewModel,
    modifier: Modifier = Modifier
) {
    val food = viewModel.currentScannedFood.collectAsStateWithLifecycle().value
    val servingMultiplier by viewModel.servingMultiplier.collectAsStateWithLifecycle()

    BackHandler {
        viewModel.navigateTo(Screen.Dashboard)
    }

    var selectedMealType by remember { mutableStateOf("Lunch") }
    var isBookmarked by remember { mutableStateOf(false) }
    var isDescriptionExpanded by remember { mutableStateOf(false) }

    if (food == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No food scanned. Please scan a meal.")
        }
        return
    }

    val scaledCalories = (food.calories * servingMultiplier).toInt()
    val scaledProtein = (food.protein * servingMultiplier).toInt()
    val scaledCarbs = (food.carbs * servingMultiplier).toInt()
    val scaledFat = (food.fat * servingMultiplier).toInt()
    val scaledFiber = (food.fiber * servingMultiplier).toInt()
    val scaledSugar = (food.sugar * servingMultiplier).toInt()
    val scaledSodium = (food.sodium * servingMultiplier).toInt()
    val scaledGrams = (food.portionGrams * servingMultiplier).toInt()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NutriWhite)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // Hero Food Image at Top
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .background(NutriBlack)
                ) {
                    if (food.imageUrl.isNotBlank()) {
                        AsyncImage(
                            model = food.imageUrl,
                            contentDescription = food.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color(0xFF1E293B), Color(0xFF0F172A))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = food.countryFlag.ifBlank { "🥗" },
                                    fontSize = 64.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.AutoAwesome,
                                        contentDescription = null,
                                        tint = NutriGreenAccent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Analyzed by Gemini AI",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = NutriGreenAccent
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Gradient overlay for contrast
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.4f),
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.6f)
                                    )
                                )
                            )
                    )

                    // Back button overlay
                    Box(
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(16.dp)
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(NutriWhite.copy(alpha = 0.9f))
                            .clickable { viewModel.navigateTo(Screen.Dashboard) }
                            .testTag("detail_back_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = NutriBlack,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Main Details Content Card
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .background(NutriWhite)
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                ) {
                    // Title and Calorie Badge Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                if (food.countryFlag.isNotBlank()) {
                                    Text(
                                        text = food.countryFlag,
                                        fontSize = 18.sp
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFF0F0F0))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "${food.cuisine} • ${food.dietaryTag}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF424242)
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = food.name,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp
                                ),
                                color = NutriBlack
                            )
                        }

                        // Flame Calorie Chip (matching mockup)
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFFFF4EE))
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocalFireDepartment,
                                contentDescription = "Calories",
                                tint = NutriFlameOrange,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "$scaledCalories kcal",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = NutriFlameOrange
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Primary Macro Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MacroDetailChip(
                            label = "Protein",
                            amount = "${scaledProtein}g",
                            accentColor = NutriProteinGreen,
                            modifier = Modifier.weight(1f)
                        )
                        MacroDetailChip(
                            label = "Carbs",
                            amount = "${scaledCarbs}g",
                            accentColor = NutriCarbsAmber,
                            modifier = Modifier.weight(1f)
                        )
                        MacroDetailChip(
                            label = "Fat",
                            amount = "${scaledFat}g",
                            accentColor = NutriFatCoral,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Secondary Nutrients Row: Fiber, Sugar, Sodium
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MacroDetailChip(
                            label = "Fiber",
                            amount = "${scaledFiber}g",
                            accentColor = Color(0xFF10B981),
                            modifier = Modifier.weight(1f)
                        )
                        MacroDetailChip(
                            label = "Sugar",
                            amount = "${scaledSugar}g",
                            accentColor = Color(0xFFF59E0B),
                            modifier = Modifier.weight(1f)
                        )
                        MacroDetailChip(
                            label = "Sodium",
                            amount = "${scaledSodium}mg",
                            accentColor = Color(0xFF6B7280),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Portion Size Adjuster Stepper
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF7F6F3))
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Portion Size",
                                style = MaterialTheme.typography.labelSmall,
                                color = NutriGray
                            )
                            Text(
                                text = "${"%.1f".format(servingMultiplier)}x serving ($scaledGrams g)",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = NutriBlack
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(NutriWhite)
                                    .border(1.dp, NutriBorder, CircleShape)
                                    .clickable {
                                        viewModel.setServingMultiplier(servingMultiplier - 0.25f)
                                    }
                                    .testTag("portion_minus_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Remove,
                                    contentDescription = "Decrease Portion",
                                    tint = NutriBlack,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(NutriBlack)
                                    .clickable {
                                        viewModel.setServingMultiplier(servingMultiplier + 0.25f)
                                    }
                                    .testTag("portion_plus_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Increase Portion",
                                    tint = NutriWhite,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Description
                    Text(
                        text = if (isDescriptionExpanded) food.description
                        else food.description.take(130) + if (food.description.length > 130) "..." else "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = NutriDarkGray,
                        lineHeight = 22.sp
                    )
                    if (food.description.length > 130) {
                        Text(
                            text = if (isDescriptionExpanded) "Read less" else "Read more",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = NutriBlack,
                            modifier = Modifier
                                .clickable { isDescriptionExpanded = !isDescriptionExpanded }
                                .padding(vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Gemini AI Health Insights Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF0F172A))
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
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
                                        text = "Gemini Nutrition Intelligence",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = NutriWhite
                                        )
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(NutriGreenAccent)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "AI VERIFIED",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 9.sp,
                                            color = NutriBlack
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Best Timing & Allergens Chips
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFF1E293B))
                                        .padding(8.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "OPTIMAL TIMING",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF94A3B8)
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = food.bestTiming,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                color = NutriWhite
                                            ),
                                            maxLines = 1
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFF1E293B))
                                        .padding(8.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "ALLERGENS",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF94A3B8)
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = food.allergens,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                color = NutriWhite
                                            ),
                                            maxLines = 1
                                        )
                                    }
                                }
                            }

                            if (food.healthBenefits.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Key Nutritional Benefits:",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFCBD5E1)
                                    )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                food.healthBenefits.forEach { benefit ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 2.dp),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(text = "•", color = NutriGreenAccent, fontWeight = FontWeight.Bold)
                                        Text(
                                            text = benefit,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = Color(0xFFE2E8F0),
                                                lineHeight = 16.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Ingredients Section (matching mockup with circular ingredient tags)
                    Text(
                        text = "Ingredients",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = NutriBlack
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        food.ingredients.take(4).forEach { ing ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFF4F3F0))
                                        .border(1.dp, NutriBorder, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = ing.take(2).uppercase(),
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = NutriBlack
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = ing,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NutriDarkGray,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Micronutrients & Minerals
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF9F9F8))
                            .border(1.dp, NutriBorder, RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = "Key Minerals & Micronutrients",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = NutriBlack
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = food.micronutrients,
                                style = MaterialTheme.typography.bodyMedium,
                                color = NutriGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Meal Type Selector
                    Text(
                        text = "Log To Meal",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = NutriBlack
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Breakfast", "Lunch", "Dinner", "Snacks").forEach { type ->
                            val isSelected = selectedMealType == type
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) NutriBlack else Color(0xFFF3F3F3))
                                    .clickable { selectedMealType = type }
                                    .padding(vertical = 10.dp)
                                    .testTag("select_meal_type_${type.lowercase()}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = type,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (isSelected) NutriWhite else NutriDarkGray
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Action Bar: "Add meal" button & Bookmark button (matching mockup)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(NutriWhite)
                .navigationBarsPadding()
                .border(1.dp, NutriBorder)
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Add meal prominent button
                Button(
                    onClick = {
                        viewModel.logCurrentFoodAsMeal(selectedMealType)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NutriBlack,
                        contentColor = NutriWhite
                    ),
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .testTag("add_meal_submit_button")
                ) {
                    Text(
                        text = "Add meal",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                // Bookmark / Save Button (matching mockup)
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(Color(0xFFF3F3F3))
                        .clickable { isBookmarked = !isBookmarked }
                        .testTag("detail_bookmark_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                        contentDescription = "Save Recipe",
                        tint = NutriBlack,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MacroDetailChip(
    label: String,
    amount: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F4F0))
            .padding(vertical = 12.dp, horizontal = 12.dp)
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = NutriGray
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = amount,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = NutriBlack
            )
        }
    }
}
