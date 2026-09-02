package com.example.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Exercise
import com.example.data.model.PresetData
import com.example.data.model.WorkoutRoutine
import com.example.ui.theme.NutriBg
import com.example.ui.theme.NutriBlack
import com.example.ui.theme.NutriBorder
import com.example.ui.theme.NutriBurnRed
import com.example.ui.theme.NutriDarkGray
import com.example.ui.theme.NutriGray
import com.example.ui.theme.NutriGreenAccent
import com.example.ui.theme.NutriWhite
import com.example.ui.viewmodel.NutriLensViewModel

@Composable
fun WorkoutScreen(
    viewModel: NutriLensViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val userGoal = profile?.goal ?: "CUT"

    var selectedGoalFilter by remember { mutableStateOf(userGoal) }
    var activeExerciseDetail by remember { mutableStateOf<Exercise?>(null) }

    val routines = remember(selectedGoalFilter) {
        if (selectedGoalFilter == "BULK") PresetData.bulkWorkouts else PresetData.cutWorkouts
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NutriBg),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                Text(
                    text = "AI Fitness Coach",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp
                    ),
                    color = NutriBlack
                )
                Text(
                    text = "Tailored routines synchronized with your caloric targets",
                    style = MaterialTheme.typography.bodyMedium,
                    color = NutriGray
                )
            }
        }

        // Goal Switcher Chips: BULK vs CUT
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEBE8E1))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf(
                    "CUT" to "Burn Fat & Cut",
                    "BULK" to "Hypertrophy & Bulk"
                ).forEach { (goalKey, title) ->
                    val isSelected = selectedGoalFilter == goalKey
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) NutriBlack else Color.Transparent)
                            .clickable { selectedGoalFilter = goalKey }
                            .padding(vertical = 10.dp)
                            .testTag("workout_filter_${goalKey.lowercase()}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (isSelected) NutriWhite else NutriDarkGray
                        )
                    }
                }
            }
        }

        // Routines List
        items(routines, key = { it.id }) { routine ->
            WorkoutRoutineCard(
                routine = routine,
                onLogWorkout = {
                    viewModel.logWorkout(routine)
                },
                onSelectExercise = { ex ->
                    activeExerciseDetail = if (activeExerciseDetail?.id == ex.id) null else ex
                },
                expandedExercise = activeExerciseDetail
            )
        }
    }
}

@Composable
fun WorkoutRoutineCard(
    routine: WorkoutRoutine,
    onLogWorkout: () -> Unit,
    onSelectExercise: (Exercise) -> Unit,
    expandedExercise: Exercise?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(NutriWhite)
            .border(1.dp, NutriBorder, RoundedCornerShape(22.dp))
            .padding(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Routine Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = routine.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = NutriBlack
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = routine.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = NutriGray
                    )
                }

                // Calorie burn badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFFFEFEF))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocalFireDepartment,
                        contentDescription = "Burn",
                        tint = NutriBurnRed,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "-${routine.estimatedCalories} cal",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = NutriBurnRed
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Duration & Level
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Timer,
                        contentDescription = "Duration",
                        tint = NutriGray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${routine.durationMinutes} min",
                        style = MaterialTheme.typography.labelMedium,
                        color = NutriDarkGray
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.FitnessCenter,
                        contentDescription = "Level",
                        tint = NutriGray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = routine.level,
                        style = MaterialTheme.typography.labelMedium,
                        color = NutriDarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Exercises in this routine
            Text(
                text = "Exercises & Form Instruction (${routine.exercises.size})",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = NutriBlack
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                routine.exercises.forEach { ex ->
                    val isExpanded = expandedExercise?.id == ex.id
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isExpanded) Color(0xFFF9F9F7) else Color(0xFFF6F6F6))
                            .border(
                                1.dp,
                                if (isExpanded) NutriBlack else Color.Transparent,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { onSelectExercise(ex) }
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = ex.name,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = NutriBlack
                                    )
                                    Text(
                                        text = "${ex.targetMuscle}  •  ${ex.sets} sets × ${ex.reps}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = NutriGray
                                    )
                                }
                                Icon(
                                    imageVector = if (isExpanded) Icons.Filled.Info else Icons.Filled.PlayArrow,
                                    contentDescription = "Form Guide",
                                    tint = NutriBlack,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Expanded Form Guide & Video Instruction details
                            if (isExpanded) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Form Execution Steps:",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = NutriBlack
                                )
                                ex.formInstructions.forEachIndexed { i, step ->
                                    Text(
                                        text = "${i + 1}. $step",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = NutriDarkGray,
                                        modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Safety Tip: ${ex.tips}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = NutriBurnRed
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Log Workout Button
            Button(
                onClick = onLogWorkout,
                colors = ButtonDefaults.buttonColors(
                    containerColor = NutriBlack,
                    contentColor = NutriWhite
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("log_workout_button_${routine.id}")
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Log Workout",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Complete & Log (-${routine.estimatedCalories} cal)",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}
