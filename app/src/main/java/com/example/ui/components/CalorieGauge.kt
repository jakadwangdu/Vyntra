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
import androidx.compose.material.icons.filled.FitnessCenter
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
    val totalAvailable = (targetCalories + caloriesBurned).coerceAtLeast(1)
    val progress = (caloriesEaten.toFloat() / totalAvailable.toFloat()).coerceIn(0f, 1f)
    val percentage = (progress * 100).toInt()

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 900),
        label = "calorie_progress"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(NutriWhite)
            .border(1.2.dp, Color(0xFFEBEBE8), RoundedCornerShape(26.dp))
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
                // Calorie Ring Gauge with Modern Glow
                Box(
                    modifier = Modifier.size(136.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(126.dp)) {
                        val strokeWidth = 11.dp.toPx()
                        // Track background
                        drawArc(
                            color = Color(0xFFF0F0EE),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                        // Progress arc
                        drawArc(
                            brush = Brush.sweepGradient(
                                listOf(
                                    NutriGreenAccent,
                                    Color(0xFF86A374),
                                    NutriGreenAccent
                                )
                            ),
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
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp,
                                letterSpacing = (-0.5).sp
                            ),
                            color = NutriBlack
                        )
                        Text(
                            text = "Calories left",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 11.sp
                            ),
                            color = NutriGray
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFEAF5E5))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "$percentage% Met",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                ),
                                color = NutriGreenAccent
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Right stats: Eaten & Burned Cards
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Eaten Stat Pill
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF7FAF6))
                            .border(1.dp, Color(0xFFE5EFE3), RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(NutriWhite)
                                .border(1.dp, Color(0xFFD6E8D3), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Restaurant,
                                contentDescription = "Eaten",
                                tint = NutriGreenAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "${caloriesEaten} kcal",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                ),
                                color = NutriBlack
                            )
                            Text(
                                text = "Intake Eaten",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = NutriGray
                            )
                        }
                    }

                    // Burned Stat Pill
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFFFF7F7))
                            .border(1.dp, Color(0xFFFFECEC), RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(NutriWhite)
                                .border(1.dp, Color(0xFFFFDEDE), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocalFireDepartment,
                                contentDescription = "Burned",
                                tint = NutriBurnRed,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "${caloriesBurned} kcal",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                ),
                                color = NutriBlack
                            )
                            Text(
                                text = "Burned Activity",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = NutriGray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Lower Row: 3 Elevated Macro Progress Columns (Carbs, Protein, Fats)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFAFAFA))
                    .border(1.dp, Color(0xFFEFEFEF), RoundedCornerShape(16.dp))
                    .padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
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
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 700),
        label = "${label}_macro"
    )

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                color = NutriDarkGray
            )
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(accentColor)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "$current",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp
                ),
                color = NutriBlack
            )
            Text(
                text = "/${target}g",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp
                ),
                color = NutriGray
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        // Modern rounded progress track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0xFFE8E8E8))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(accentColor)
            )
        }
    }
}
