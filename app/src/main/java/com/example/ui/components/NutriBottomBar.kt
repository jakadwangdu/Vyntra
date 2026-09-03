package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NutriBlack
import com.example.ui.theme.NutriDarkGray
import com.example.ui.theme.NutriWhite
import com.example.ui.viewmodel.Screen

@Composable
fun NutriBottomBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 20.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .shadow(16.dp, RoundedCornerShape(34.dp))
                .height(68.dp)
                .clip(RoundedCornerShape(34.dp))
                .background(NutriBlack)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val isHome = currentScreen is Screen.Dashboard
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (isHome) NutriDarkGray else Color.Transparent)
                    .clickable { onNavigate(Screen.Dashboard) }
                    .testTag("nav_home_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = "Home",
                    tint = NutriWhite,
                    modifier = Modifier.size(26.dp)
                )
            }

            // Center White Add Button
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(NutriWhite)
                    .clickable { onNavigate(Screen.Scanner) }
                    .testTag("nav_camera_scan_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Scan Meal",
                    tint = NutriBlack,
                    modifier = Modifier.size(28.dp)
                )
            }

            val isRecipes = currentScreen is Screen.Recipes
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (isRecipes) NutriDarkGray else Color.Transparent)
                    .clickable { onNavigate(Screen.Recipes) }
                    .testTag("nav_recipes_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.PieChart,
                    contentDescription = "Diet",
                    tint = NutriWhite,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

