package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
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
import com.example.ui.components.GestureNavigationHost
import com.example.ui.components.GlobalSearchOverlay
import com.example.ui.components.NutriBottomBar
import com.example.ui.components.primaryScreens
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
    val isAiFoodSearching by viewModel.isAiFoodSearching.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NutriBottomBar(
                    currentScreen = currentScreen,
                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                )
            }
        }
    ) { innerPadding ->
        GestureNavigationHost(
            currentScreen = currentScreen,
            onNavigate = { screen -> viewModel.navigateTo(screen) },
            onOpenSearch = { showGlobalSearch = true },
            onOpenScanner = { viewModel.navigateTo(Screen.Scanner) },
            modifier = Modifier.fillMaxSize()
        ) { targetScreen ->
            AnimatedContent(
                targetState = targetScreen,
                transitionSpec = {
                    val initialIndex = primaryScreens.indexOf(initialState)
                    val targetIndex = primaryScreens.indexOf(targetState)
                    val slideLeft = if (initialIndex != -1 && targetIndex != -1) {
                        targetIndex > initialIndex
                    } else {
                        targetState is Screen.Scanner || targetState is Screen.FoodDetail || targetState is Screen.Chatbot
                    }

                    if (slideLeft) {
                        (slideInHorizontally(
                            initialOffsetX = { fullWidth -> fullWidth },
                            animationSpec = tween(320, easing = FastOutSlowInEasing)
                        ) + fadeIn(tween(320))).togetherWith(
                            slideOutHorizontally(
                                targetOffsetX = { fullWidth -> -fullWidth / 3 },
                                animationSpec = tween(320, easing = FastOutSlowInEasing)
                            ) + fadeOut(tween(250))
                        )
                    } else {
                        (slideInHorizontally(
                            initialOffsetX = { fullWidth -> -fullWidth },
                            animationSpec = tween(320, easing = FastOutSlowInEasing)
                        ) + fadeIn(tween(320))).togetherWith(
                            slideOutHorizontally(
                                targetOffsetX = { fullWidth -> fullWidth / 3 },
                                animationSpec = tween(320, easing = FastOutSlowInEasing)
                            ) + fadeOut(tween(250))
                        )
                    }
                },
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
                onDismiss = { showGlobalSearch = false },
                onSelectFood = { food ->
                    viewModel.selectPresetFood(food)
                },
                onAnalyzeFoodQuery = { query ->
                    viewModel.searchAndAnalyzeFoodName(query)
                },
                isAiSearching = isAiFoodSearching
            )
        }
    }
}
