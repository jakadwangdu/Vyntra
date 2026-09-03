package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.data.model.CustomDietPlan
import com.example.data.model.DietPlanMeal
import com.example.ui.theme.NutriBg
import com.example.ui.theme.NutriBlack
import com.example.ui.theme.NutriBorder
import com.example.ui.theme.NutriDarkGray
import com.example.ui.theme.NutriGray
import com.example.ui.theme.NutriGreenAccent
import com.example.ui.theme.NutriWhite
import com.example.ui.viewmodel.NutriLensViewModel
import com.example.ui.viewmodel.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietPlanScreen(
    viewModel: NutriLensViewModel,
    modifier: Modifier = Modifier
) {
    val dietPlan by viewModel.customDietPlan.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    var loggedMealTitle by remember { mutableStateOf<String?>(null) }

    BackHandler {
        viewModel.navigateTo(Screen.Dashboard)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = NutriBg,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Custom AI Diet Plan",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = NutriBlack
                        )
                        val subtitle = buildString {
                            append(when (dietPlan?.goal) {
                                "BULK" -> "Bulk (Hypertrophy Surplus)"
                                "LOSE_WEIGHT" -> "Cut (Caloric Deficit)"
                                else -> "Physique Maintenance"
                            })
                            dietPlan?.dietaryPreference?.let {
                                append(" • $it")
                            }
                        }
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = NutriGray
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(Screen.Dashboard) },
                        modifier = Modifier.testTag("diet_plan_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Dashboard",
                            tint = NutriBlack
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.navigateTo(Screen.Onboarding) },
                        modifier = Modifier.testTag("diet_plan_edit_profile_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile & Targets",
                            tint = NutriBlack
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = NutriBg
                )
            )
        }
    ) { padding ->
        val plan = dietPlan
        if (plan == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "No Diet Plan Configured",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = NutriBlack
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Configure your profile to receive an AI-calibrated macro diet plan.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = NutriGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.navigateTo(Screen.Onboarding) },
                        colors = ButtonDefaults.buttonColors(containerColor = NutriBlack)
                    ) {
                        Text("Open Profile Calibration")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Success Toast/Banner when user logs a meal
                loggedMealTitle?.let { title ->
                    item {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = NutriGreenAccent),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = NutriBlack,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Logged \"$title\" to your daily nutrition!",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = NutriBlack
                                )
                            }
                        }
                    }
                }

                // Caloric & Macro Target Hero Card
                item {
                    DietPlanHeroCard(plan = plan)
                }

                // AI Metabolic Analysis Rationale
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = NutriWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NutriBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = null,
                                    tint = NutriBlack,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "AI Metabolic Analysis",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = NutriBlack
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = plan.analysisSummary,
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                                color = NutriDarkGray
                            )
                        }
                    }
                }

                // Actionable Strategy Tips
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = NutriWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NutriBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Daily Nutritional Guidelines",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = NutriBlack
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            plan.strategyTips.forEachIndexed { idx, tip ->
                                Row(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(NutriBlack),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${idx + 1}",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = NutriWhite
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = tip,
                                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                        color = NutriDarkGray,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }

                // Section Header: Tailored Daily Meals
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tailored Daily Meals (${plan.meals.size})",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = NutriBlack
                        )
                        Text(
                            text = plan.dietaryPreference,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NutriGreenAccent
                            )
                        )
                    }
                }

                // List of Tailored Meals
                items(plan.meals) { meal ->
                    DietPlanMealCard(
                        meal = meal,
                        onLogClick = {
                            viewModel.logDietPlanMeal(meal)
                            loggedMealTitle = meal.title
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

@Composable
private fun DietPlanHeroCard(plan: CustomDietPlan) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = NutriBlack),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NutriGreenAccent)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = when (plan.goal) {
                            "BULK" -> "HYPERTROPHY BULK (+400 kcal)"
                            "LOSE_WEIGHT" -> "DEFICIT CUT (-500 kcal)"
                            else -> "EQUILIBRIUM MAINTENANCE"
                        },
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        ),
                        color = NutriBlack
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalDrink,
                        contentDescription = null,
                        tint = NutriGreenAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${plan.hydrationTargetFlOz} fl oz / day",
                        style = MaterialTheme.typography.labelSmall,
                        color = NutriWhite.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "${plan.targetCalories}",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 40.sp,
                            color = NutriWhite
                        )
                    )
                    Text(
                        text = "Daily Calorie Intake Target",
                        style = MaterialTheme.typography.bodySmall,
                        color = NutriWhite.copy(alpha = 0.75f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    MacroHeroStat("Protein", "${plan.targetProtein}g")
                    MacroHeroStat("Carbs", "${plan.targetCarbs}g")
                    MacroHeroStat("Fats", "${plan.targetFat}g")
                }
            }
        }
    }
}

@Composable
private fun MacroHeroStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = NutriWhite
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = NutriWhite.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun DietPlanMealCard(
    meal: DietPlanMeal,
    onLogClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = NutriWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, NutriBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(NutriBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = meal.mealType.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = NutriBlack
                    )
                }

                Text(
                    text = "${meal.calories} kcal • ${meal.portion}",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = NutriBlack
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = meal.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = NutriBlack
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = meal.preparation,
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                color = NutriDarkGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Macros Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MacroPill("P: ${meal.protein}g")
                MacroPill("C: ${meal.carbs}g")
                MacroPill("F: ${meal.fat}g")
                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onLogClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NutriBlack,
                        contentColor = NutriWhite
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("log_meal_${meal.mealType.lowercase()}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Log Meal",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Ingredients
            Text(
                text = "Ingredients: " + meal.ingredients.joinToString(" • "),
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = NutriGray
            )
        }
    }
}

@Composable
private fun MacroPill(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(NutriBg)
            .border(1.dp, NutriBorder, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = NutriBlack
        )
    }
}
