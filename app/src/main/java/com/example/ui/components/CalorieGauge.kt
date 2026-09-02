package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NutriBlack
import com.example.ui.theme.NutriBorder
import com.example.ui.theme.NutriBurnRed
import com.example.ui.theme.NutriCarbsAmber
import com.example.ui.theme.NutriDarkGray
import com.example.ui.theme.NutriFatCoral
import com.example.ui.theme.NutriFlameOrange
import com.example.ui.theme.NutriGray
import com.example.ui.theme.NutriGreenAccent
import com.example.ui.theme.NutriProteinGreen
import com.example.ui.theme.NutriWhite

@Composable
fun CalorieSummaryCard(
    caloriesLeft: Int,
    caloriesEaten: Int,
    caloriesBurned: Int,
    targetCalories: Int,
    carbsGrams: Int,
    targetCarbs: Int,
    proteinGrams: Int,
    targetProtein: Int,
    fatGrams: Int,
    targetFat: Int,
    modifier: Modifier = Modifier
) {
    val progress = if (targetCalories > 0) {
        (caloriesEaten.toFloat() / targetCalories.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 800),
        label = "calorie_progress"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(NutriWhite)
            .border(1.dp, NutriBorder, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Upper Row: Ring Gauge on left, Eaten & Burned stats on right
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Calorie Ring Gauge
                Box(
                    modifier = Modifier.size(130.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(120.dp)) {
                        val strokeWidth = 10.dp.toPx()
                        // Track background
                        drawArc(
                            color = Color(0xFFEFEFEF),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                        // Progress arc (olive green from mockup)
                        drawArc(
                            color = NutriGreenAccent,
                            startAngle = -90f,
                            sweepAngle = animatedProgress * 360f,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = String.format("%,d", caloriesLeft),
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            ),
                            color = NutriBlack
                        )
                        Text(
                            text = "Calories left",
                            style = MaterialTheme.typography.labelSmall,
                            color = NutriGray
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Right stats: Eaten & Burned
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Eaten Stat
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(Color(0xFFE8F3E8), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Restaurant,
                                contentDescription = "Eaten",
                                tint = NutriGreenAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "${caloriesEaten} cal",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = NutriBlack
                            )
                            Text(
                                text = "Eaten",
                                style = MaterialTheme.typography.labelSmall,
                                color = NutriGray
                            )
                        }
                    }

                    // Burned Stat
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(Color(0xFFFFEFEF), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocalFireDepartment,
                                contentDescription = "Burned",
                                tint = NutriBurnRed,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "${caloriesBurned} cal",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = NutriBlack
                            )
                            Text(
                                text = "Burned",
                                style = MaterialTheme.typography.labelSmall,
                                color = NutriGray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Lower Row: 3 Macro Progress Bars (Carbs, Protein, Fats)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MacroColumnItem(
                    label = "Carbs",
                    current = carbsGrams,
                    target = targetCarbs,
                    accentColor = NutriCarbsAmber,
                    modifier = Modifier.weight(1f)
                )
                MacroColumnItem(
                    label = "Protein",
                    current = proteinGrams,
                    target = targetProtein,
                    accentColor = NutriProteinGreen,
                    modifier = Modifier.weight(1f)
                )
                MacroColumnItem(
                    label = "Fats",
                    current = fatGrams,
                    target = targetFat,
                    accentColor = NutriFatCoral,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun MacroColumnItem(
    label: String,
    current: Int,
    target: Int,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val progress = if (target > 0) (current.toFloat() / target.toFloat()).coerceIn(0f, 1f) else 0f

    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = NutriDarkGray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "$current",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = NutriBlack
            )
            Text(
                text = "/${target}g",
                style = MaterialTheme.typography.labelSmall,
                color = NutriGray
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(Color(0xFFEEEEEE), RoundedCornerShape(3.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(6.dp)
                    .background(accentColor, RoundedCornerShape(3.dp))
            )
        }
    }
}
