package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import com.example.ui.components.GlobalSearchOverlay
import com.example.ui.components.NutriBottomBar
import com.example.ui.screens.ChatbotScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DietPlanScreen
import com.example.ui.screens.FoodDetailScreen
import com.example.ui.screens.FoodScannerScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.RecipesScreen
import com.example.ui.screens.WorkoutScreen
import com.example.ui.theme.NutriLensTheme
import com.example.ui.viewmodel.NutriLensViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutriLensTheme {
                val viewModel: NutriLensViewModel = viewModel()
                NutriLensApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun NutriLensApp(viewModel: NutriLensViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val showBottomBar = currentScreen is Screen.Dashboard ||
            currentScreen is Screen.Recipes ||
            currentScreen is Screen.Workout

    var showGlobalSearch by remember { mutableStateOf(false) }
    var startY by remember { mutableStateOf(0f) }
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val topEighthPx = remember(configuration, density) {
        with(density) { (configuration.screenHeightDp / 8).dp.toPx() }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        val pointer = event.changes.firstOrNull()
                        if (pointer != null) {
                            if (pointer.pressed && !pointer.previousPressed) {
                                startY = pointer.position.y
                            } else if (pointer.pressed && pointer.previousPressed) {
                                val currentY = pointer.position.y
                                if (startY <= topEighthPx && (currentY - startY) > 50f) {
                                    showGlobalSearch = true
                                }
                            }
                        }
                    }
                }
            },
        bottomBar = {
            if (showBottomBar) {
                NutriBottomBar(
                    currentScreen = currentScreen,
                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Crossfade(
                targetState = currentScreen,
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    is Screen.Dashboard -> DashboardScreen(viewModel = viewModel)
                    is Screen.Scanner -> FoodScannerScreen(viewModel = viewModel)
                    is Screen.FoodDetail -> FoodDetailScreen(viewModel = viewModel)
                    is Screen.Recipes -> RecipesScreen(viewModel = viewModel)
                    is Screen.Workout -> WorkoutScreen(viewModel = viewModel)
                    is Screen.DietPlan -> DietPlanScreen(viewModel = viewModel)
                    is Screen.Chatbot -> ChatbotScreen(viewModel = viewModel)
                    is Screen.Onboarding -> OnboardingScreen(viewModel = viewModel)
                }
            }

            GlobalSearchOverlay(
                isVisible = showGlobalSearch,
                onDismiss = { showGlobalSearch = false }
            )
        }
    }
}
