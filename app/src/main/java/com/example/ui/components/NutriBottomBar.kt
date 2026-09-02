package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NutriBlack
import com.example.ui.theme.NutriBorder
import com.example.ui.theme.NutriBurnRed
import com.example.ui.theme.NutriDarkGray
import com.example.ui.theme.NutriWhite
import com.example.ui.viewmodel.Screen

@Composable
fun NutriBottomBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = NutriWhite,
        tonalElevation = 6.dp,
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home / Dashboard
                val isHome = currentScreen is Screen.Dashboard
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable { onNavigate(Screen.Dashboard) }
                        .testTag("nav_home_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isHome) Icons.Filled.Home else Icons.Outlined.Home,
                        contentDescription = "Home",
                        tint = if (isHome) NutriBlack else Color(0xFF9E9E9E),
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Recipes / Search
                val isRecipes = currentScreen is Screen.Recipes
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable { onNavigate(Screen.Recipes) }
                        .testTag("nav_recipes_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isRecipes) Icons.Filled.RestaurantMenu else Icons.Outlined.RestaurantMenu,
                        contentDescription = "Recipes",
                        tint = if (isRecipes) NutriBlack else Color(0xFF9E9E9E),
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Center Spacer for elevated Scan button
                Box(modifier = Modifier.size(48.dp))

                // Fitness / Workouts
                val isWorkout = currentScreen is Screen.Workout
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable { onNavigate(Screen.Workout) }
                        .testTag("nav_workouts_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isWorkout) Icons.Filled.FitnessCenter else Icons.Outlined.FitnessCenter,
                        contentDescription = "Workouts",
                        tint = if (isWorkout) NutriBlack else Color(0xFF9E9E9E),
                        modifier = Modifier.size(24.dp)
                    )
                }

                // AI Coach Chatbot
                val isChat = currentScreen is Screen.Chatbot
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable { onNavigate(Screen.Chatbot) }
                        .testTag("nav_chat_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isChat) Icons.Filled.Chat else Icons.Outlined.Chat,
                        contentDescription = "AI Coach",
                        tint = if (isChat) NutriBlack else Color(0xFF9E9E9E),
                        modifier = Modifier.size(24.dp)
                    )
                    // Active indicator dot
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-8).dp, y = 8.dp)
                            .size(7.dp)
                            .background(NutriBurnRed, CircleShape)
                    )
                }
            }

            // Elevated Center Shutter / Scan Button (matching screenshot 1 center button)
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = (-12).dp)
                    .size(56.dp)
                    .shadow(10.dp, CircleShape)
                    .background(NutriBlack, CircleShape)
                    .clip(CircleShape)
                    .clickable { onNavigate(Screen.Scanner) }
                    .testTag("nav_camera_scan_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Scan Meal",
                    tint = NutriWhite,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}
