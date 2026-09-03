package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.model.DietPlanGenerator
import com.example.ui.theme.NutriBg
import com.example.ui.theme.NutriBlack
import com.example.ui.theme.NutriBorder
import com.example.ui.theme.NutriDarkGray
import com.example.ui.theme.NutriGray
import com.example.ui.theme.NutriGreenAccent
import com.example.ui.theme.NutriWhite
import com.example.ui.viewmodel.NutriLensViewModel

@Composable
fun OnboardingScreen(
    viewModel: NutriLensViewModel,
    modifier: Modifier = Modifier
) {
    val existingProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    var name by remember(existingProfile) { mutableStateOf(existingProfile?.name ?: "Athlete") }
    var ageStr by remember(existingProfile) { mutableStateOf(existingProfile?.age?.toString() ?: "25") }
    var gender by remember(existingProfile) { mutableStateOf(existingProfile?.gender ?: "Male") }
    var weightStr by remember(existingProfile) { mutableStateOf(existingProfile?.weightKg?.toInt()?.toString() ?: "75") }
    var heightStr by remember(existingProfile) { mutableStateOf(existingProfile?.heightCm?.toInt()?.toString() ?: "178") }
    var activityLevel by remember(existingProfile) { mutableStateOf(existingProfile?.activityLevel ?: "Moderate") }
    var selectedGoal by remember(existingProfile) { mutableStateOf(existingProfile?.goal ?: "MAINTAIN") }
    var dietaryPreference by remember(existingProfile) { mutableStateOf(existingProfile?.dietaryRestriction ?: "Both Veg & Non-Veg") }
    var useCustomCalories by remember { mutableStateOf(false) }
    var customCaloriesStr by remember(existingProfile) { mutableStateOf(existingProfile?.dailyCalorieTarget?.toString() ?: "2400") }

    val age = ageStr.toIntOrNull() ?: 25
    val weight = weightStr.toFloatOrNull() ?: 75f
    val height = heightStr.toFloatOrNull() ?: 178f
    val customCal = if (useCustomCalories) customCaloriesStr.toIntOrNull() else null

    val previewMetrics by remember(age, gender, weight, height, activityLevel, selectedGoal, customCal) {
        derivedStateOf {
            DietPlanGenerator.calculateMetrics(
                age = age,
                gender = gender,
                weightKg = weight,
                heightCm = height,
                activityLevel = activityLevel,
                goal = selectedGoal,
                customCalorieTarget = customCal
            )
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NutriBg),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_vyntra_logo),
                        contentDescription = "Vyntra Logo",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "VYNTRA NUTRITION",
                        style = MaterialTheme.typography.titleMedium.copy(
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Black,
                            color = NutriBlack
                        )
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Personalized Body Profile & Custom Diet Plan",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    color = NutriBlack
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Provide your stats to receive an AI-calibrated macro diet plan tailored precisely to bulk, maintain, or lose weight.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = NutriGray
                )
            }
        }

        // Live Calculated Targets Preview Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = NutriBlack),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = NutriGreenAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "CALIBRATED TARGETS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.2.sp,
                                    color = NutriGreenAccent
                                )
                            )
                        }
                        Text(
                            text = "TDEE: ${previewMetrics.tdee} kcal",
                            style = MaterialTheme.typography.labelSmall,
                            color = NutriWhite.copy(alpha = 0.7f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "${previewMetrics.targetCalories}",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 36.sp,
                                    color = NutriWhite
                                )
                            )
                            Text(
                                text = "Daily Calorie Target",
                                style = MaterialTheme.typography.bodySmall,
                                color = NutriWhite.copy(alpha = 0.75f)
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            MacroPreviewBadge("Protein", "${previewMetrics.proteinGrams}g")
                            MacroPreviewBadge("Carbs", "${previewMetrics.carbsGrams}g")
                            MacroPreviewBadge("Fats", "${previewMetrics.fatGrams}g")
                        }
                    }
                }
            }
        }

        // Full Name
        item {
            Column {
                Text("Full Name", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("e.g. Alex Hunter") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("onboarding_name_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = NutriWhite,
                        unfocusedContainerColor = NutriWhite,
                        focusedBorderColor = NutriBlack,
                        unfocusedBorderColor = NutriBorder
                    )
                )
            }
        }

        // Gender Selection
        item {
            Column {
                Text(
                    "Gender",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Male", "Female", "Other").forEach { g ->
                        val isSelected = gender == g
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) NutriBlack else NutriWhite)
                                .border(1.dp, if (isSelected) NutriBlack else NutriBorder, RoundedCornerShape(12.dp))
                                .clickable { gender = g }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = g,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isSelected) NutriWhite else NutriBlack
                            )
                        }
                    }
                }
            }
        }

        // Age, Weight, Height Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Age", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = ageStr,
                        onValueChange = { ageStr = it },
                        modifier = Modifier.fillMaxWidth().testTag("onboarding_age_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = NutriWhite,
                            unfocusedContainerColor = NutriWhite
                        )
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("Weight (kg)", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = weightStr,
                        onValueChange = { weightStr = it },
                        modifier = Modifier.fillMaxWidth().testTag("onboarding_weight_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = NutriWhite,
                            unfocusedContainerColor = NutriWhite
                        )
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("Height (cm)", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = heightStr,
                        onValueChange = { heightStr = it },
                        modifier = Modifier.fillMaxWidth().testTag("onboarding_height_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = NutriWhite,
                            unfocusedContainerColor = NutriWhite
                        )
                    )
                }
            }
        }

        // Primary Fitness Goal: Bulk, Maintain, Lose Weight
        item {
            Column {
                Text(
                    "What is your primary fitness goal?",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        Triple("BULK", "Bulk (Gain Lean Muscle)", "Caloric surplus (+400 kcal) with high protein & clean carbs for hypertrophy."),
                        Triple("MAINTAIN", "Maintain Physique & Fitness", "Energy balance to sustain lean body mass, stamina, and metabolic performance."),
                        Triple("LOSE_WEIGHT", "Lose Weight & Cut Fat", "Caloric deficit (-500 kcal) with muscle-sparing high protein to strip fat.")
                    ).forEach { (goalKey, title, desc) ->
                        val isSelected = selectedGoal == goalKey
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) NutriBlack else NutriWhite)
                                .border(1.dp, if (isSelected) NutriBlack else NutriBorder, RoundedCornerShape(14.dp))
                                .clickable { selectedGoal = goalKey }
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) NutriGreenAccent else NutriBg)
                                        .border(1.dp, if (isSelected) NutriGreenAccent else NutriBorder, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = NutriBlack,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (isSelected) NutriWhite else NutriBlack
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = desc,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isSelected) NutriWhite.copy(alpha = 0.75f) else NutriGray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Dietary Preference: Vegetarian, Vegan, Non-Vegetarian, Both Veg & Non-Veg
        item {
            Column {
                Text(
                    "Dietary Preference",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        Pair("Vegetarian", "Dairy, paneer, eggs, legumes, grains (no meat or fish)"),
                        Pair("Vegan", "100% plant-based (tofu, tempeh, beans, seeds, greens)"),
                        Pair("Non-Vegetarian", "Chicken breast, salmon, eggs, turkey, lean beef & fish"),
                        Pair("Both Veg & Non-Veg", "Flexitarian balanced diet (plant foods + poultry & fish)")
                    ).forEach { (diet, desc) ->
                        val isSelected = dietaryPreference == diet
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) NutriGreenAccent else NutriWhite)
                                .border(1.dp, if (isSelected) NutriGreenAccent else NutriBorder, RoundedCornerShape(14.dp))
                                .clickable { dietaryPreference = diet }
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Restaurant,
                                    contentDescription = null,
                                    tint = if (isSelected) NutriBlack else NutriDarkGray,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = diet,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = NutriBlack
                                    )
                                    Text(
                                        text = desc,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isSelected) NutriBlack.copy(alpha = 0.8f) else NutriGray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Activity Level
        item {
            Column {
                Text(
                    "Daily Activity Level",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Sedentary", "Moderate", "Active", "Very Active").forEach { lvl ->
                        val isSelected = activityLevel == lvl
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) NutriBlack else NutriWhite)
                                .border(1.dp, if (isSelected) NutriBlack else NutriBorder, RoundedCornerShape(12.dp))
                                .clickable { activityLevel = lvl }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = lvl,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isSelected) NutriWhite else NutriBlack,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        // Custom Calorie Intake Option
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NutriWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, NutriBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Set Custom Calorie Intake",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = NutriBlack
                            )
                            Text(
                                text = "Override auto metabolic calculation with your own daily kcal target",
                                style = MaterialTheme.typography.bodySmall,
                                color = NutriGray
                            )
                        }
                        Switch(
                            checked = useCustomCalories,
                            onCheckedChange = { useCustomCalories = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = NutriWhite,
                                checkedTrackColor = NutriGreenAccent
                            )
                        )
                    }

                    AnimatedVisibility(visible = useCustomCalories) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            OutlinedTextField(
                                value = customCaloriesStr,
                                onValueChange = { customCaloriesStr = it },
                                label = { Text("Custom Daily Target (kcal)") },
                                placeholder = { Text("e.g. 2500") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = NutriBg,
                                    unfocusedContainerColor = NutriBg
                                )
                            )
                        }
                    }
                }
            }
        }

        // Save & Generate Diet Plan Button
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    viewModel.updateProfile(
                        name = name,
                        age = age,
                        gender = gender,
                        weightKg = weight,
                        heightCm = height,
                        activityLevel = activityLevel,
                        goal = selectedGoal,
                        dietaryPreference = dietaryPreference,
                        customCalorieTarget = customCal
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = NutriBlack,
                    contentColor = NutriWhite
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("onboarding_save_button")
            ) {
                Text(
                    text = "Generate Custom Diet Plan",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Save and generate custom diet plan"
                )
            }
        }
    }
}

@Composable
private fun MacroPreviewBadge(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge.copy(
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
