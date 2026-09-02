package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.NutriBg
import com.example.ui.theme.NutriBlack
import com.example.ui.theme.NutriBorder
import com.example.ui.theme.NutriDarkGray
import com.example.ui.theme.NutriGray
import com.example.ui.theme.NutriGreenAccent
import com.example.ui.theme.NutriWhite
import com.example.ui.viewmodel.NutriLensViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun OnboardingScreen(
    viewModel: NutriLensViewModel,
    modifier: Modifier = Modifier
) {
    val existingProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    var name by remember(existingProfile) { mutableStateOf(existingProfile?.name ?: "Yohanan Rashad") }
    var ageStr by remember(existingProfile) { mutableStateOf(existingProfile?.age?.toString() ?: "26") }
    var weightStr by remember(existingProfile) { mutableStateOf(existingProfile?.weightKg?.toString() ?: "76") }
    var heightStr by remember(existingProfile) { mutableStateOf(existingProfile?.heightCm?.toString() ?: "180") }
    var selectedGoal by remember(existingProfile) { mutableStateOf(existingProfile?.goal ?: "CUT") }
    var activityLevel by remember(existingProfile) { mutableStateOf(existingProfile?.activityLevel ?: "Moderate") }
    var dietaryRestriction by remember(existingProfile) { mutableStateOf(existingProfile?.dietaryRestriction ?: "None") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NutriBg),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 48.dp),
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
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "VYNTRA",
                        style = MaterialTheme.typography.titleMedium.copy(
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Black,
                            color = NutriBlack
                        )
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Personal Profile & Caloric Calibration",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp
                    ),
                    color = NutriBlack
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Enter your body statistics so our AI engine can calculate optimal macro and calorie targets.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = NutriGray
                )
            }
        }

        // Name
        item {
            Column {
                Text("Full Name", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
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

        // Age & Weight & Height
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
                        modifier = Modifier.fillMaxWidth(),
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
                        modifier = Modifier.fillMaxWidth(),
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
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = NutriWhite,
                            unfocusedContainerColor = NutriWhite
                        )
                    )
                }
            }
        }

        // Primary Goal (BULK vs CUT vs MAINTAIN)
        item {
            Column {
                Text(
                    "Primary Fitness Goal",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        "CUT" to "Cut (Deficit)",
                        "BULK" to "Bulk (Surplus)",
                        "MAINTAIN" to "Maintain"
                    ).forEach { (goalKey, label) ->
                        val isSelected = selectedGoal == goalKey
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) NutriBlack else NutriWhite)
                            .border(1.dp, if (isSelected) NutriBlack else NutriBorder, RoundedCornerShape(14.dp))
                            .clickable { selectedGoal = goalKey }
                            .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isSelected) NutriWhite else NutriBlack
                            )
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
                                .background(if (isSelected) NutriGreenAccent else NutriWhite)
                                .border(1.dp, if (isSelected) NutriGreenAccent else NutriBorder, RoundedCornerShape(12.dp))
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

        // Dietary Restriction
        item {
            Column {
                Text(
                    "Dietary Preference",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("None", "Vegan", "Vegetarian", "Keto").forEach { diet ->
                        val isSelected = dietaryRestriction == diet
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) NutriBlack else NutriWhite)
                                .border(1.dp, if (isSelected) NutriBlack else NutriBorder, RoundedCornerShape(12.dp))
                                .clickable { dietaryRestriction = diet }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = diet,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isSelected) NutriWhite else NutriBlack
                            )
                        }
                    }
                }
            }
        }

        // Save & Calculate Target Button
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    val age = ageStr.toIntOrNull() ?: 26
                    val weight = weightStr.toFloatOrNull() ?: 76f
                    val height = heightStr.toFloatOrNull() ?: 180f
                    viewModel.updateProfile(
                        name = name,
                        age = age,
                        weightKg = weight,
                        heightCm = height,
                        activityLevel = activityLevel,
                        goal = selectedGoal,
                        restriction = dietaryRestriction
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
                    text = "Save Profile & Generate Plan",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Save and continue"
                )
            }
        }
    }
}
