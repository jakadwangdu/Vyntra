package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NutriBlack
import com.example.ui.theme.NutriGreenAccent
import com.example.ui.theme.NutriWhite
import com.example.ui.viewmodel.Screen
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

// Ordered list of primary pages for horizontal swipe navigation
val primaryScreens = listOf(
    Screen.Dashboard,
    Screen.Recipes,
    Screen.Workout,
    Screen.DietPlan,
    Screen.Chatbot
)

@Composable
fun GestureNavigationHost(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    onOpenSearch: () -> Unit,
    onOpenScanner: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (Screen) -> Unit
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    // Bottom trigger zone: bottom 25% of screen
    val bottomZoneStartPx = screenHeightPx * 0.75f
    // Top trigger zone: top 15% of screen
    val topZoneEndPx = screenHeightPx * 0.15f
    // Half screen swipe distance threshold for camera
    val halfScreenThresholdPx = screenHeightPx * 0.40f

    // Visual bottom pull indicator for camera
    var isSwipingUpForCamera by remember { mutableStateOf(false) }
    var cameraPullFraction by remember { mutableFloatStateOf(0f) }

    // Offset animatable for horizontal swipe page transitions
    val horizontalOffset = remember { Animatable(0f) }

    val currentIndex = remember(currentScreen) {
        primaryScreens.indexOf(currentScreen)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(currentScreen, currentIndex, screenHeightPx, screenWidthPx) {
                var totalDragX = 0f
                var totalDragY = 0f
                var initialTouchY = 0f
                var initialTouchX = 0f
                var isHorizontalSwipe = false
                var isBottomSwipeUp = false
                var isTopSwipeDown = false
                var gestureLocked = false

                detectDragGestures(
                    onDragStart = { offset ->
                        totalDragX = 0f
                        totalDragY = 0f
                        initialTouchX = offset.x
                        initialTouchY = offset.y
                        isHorizontalSwipe = false
                        isBottomSwipeUp = false
                        isTopSwipeDown = false
                        gestureLocked = false
                    },
                    onDrag = { change, dragAmount ->
                        totalDragX += dragAmount.x
                        totalDragY += dragAmount.y

                        val absX = abs(totalDragX)
                        val absY = abs(totalDragY)

                        if (!gestureLocked) {
                            if (absX > 15f || absY > 15f) {
                                gestureLocked = true
                                if (absY > absX) {
                                    // Vertical gesture
                                    if (initialTouchY >= bottomZoneStartPx && totalDragY < 0) {
                                        isBottomSwipeUp = true
                                    } else if (initialTouchY <= topZoneEndPx && totalDragY > 0) {
                                        isTopSwipeDown = true
                                    }
                                } else {
                                    // Horizontal gesture - allow if on primary tab screens or detail screen
                                    if (currentIndex != -1 || currentScreen is Screen.FoodDetail) {
                                        isHorizontalSwipe = true
                                    }
                                }
                            }
                        }

                        if (isBottomSwipeUp && currentScreen !is Screen.Scanner) {
                            change.consume()
                            val upwardDistance = -totalDragY
                            val fraction = (upwardDistance / halfScreenThresholdPx).coerceIn(0f, 1f)
                            cameraPullFraction = fraction
                            isSwipingUpForCamera = true
                        } else if (isHorizontalSwipe) {
                            // Provide subtle tactile drag offset for page switching
                            if (absX > 25f && absX > absY * 1.5f) {
                                change.consume()
                                coroutineScope.launch {
                                    horizontalOffset.snapTo((totalDragX * 0.35f).coerceIn(-screenWidthPx * 0.4f, screenWidthPx * 0.4f))
                                }
                            }
                        }
                    },
                    onDragEnd = {
                        if (isBottomSwipeUp && currentScreen !is Screen.Scanner) {
                            if (cameraPullFraction >= 0.75f || -totalDragY >= halfScreenThresholdPx) {
                                onOpenScanner()
                            }
                        } else if (isTopSwipeDown) {
                            if (totalDragY > 70f) {
                                onOpenSearch()
                            }
                        } else if (isHorizontalSwipe) {
                            val minSwipeDistance = screenWidthPx * 0.18f
                            if (totalDragX < -minSwipeDistance) {
                                // Swiped Left -> Move to Next Screen
                                if (currentIndex != -1 && currentIndex < primaryScreens.size - 1) {
                                    onNavigate(primaryScreens[currentIndex + 1])
                                }
                            } else if (totalDragX > minSwipeDistance) {
                                // Swiped Right -> Move to Previous Screen or back to Dashboard
                                if (currentScreen is Screen.FoodDetail || currentScreen is Screen.Scanner) {
                                    onNavigate(Screen.Dashboard)
                                } else if (currentIndex > 0) {
                                    onNavigate(primaryScreens[currentIndex - 1])
                                }
                            }
                        }

                        // Reset states smoothly
                        coroutineScope.launch {
                            horizontalOffset.animateTo(0f, tween(250))
                        }
                        isSwipingUpForCamera = false
                        cameraPullFraction = 0f
                    },
                    onDragCancel = {
                        coroutineScope.launch {
                            horizontalOffset.animateTo(0f, tween(250))
                        }
                        isSwipingUpForCamera = false
                        cameraPullFraction = 0f
                    }
                )
            }
    ) {
        // Main Screen Content with horizontal gesture offset
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(horizontalOffset.value.roundToInt(), 0) }
        ) {
            content(currentScreen)
        }

        // Camera Swipe-Up Indicator & Pill (visual cues during swipe up from bottom to half screen)
        if (isSwipingUpForCamera && currentScreen !is Screen.Scanner) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = (cameraPullFraction * 0.45f))),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(bottom = (60.dp + (180.dp * cameraPullFraction)))
                        .alpha((cameraPullFraction * 1.4f).coerceIn(0f, 1f))
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(if (cameraPullFraction >= 0.75f) NutriGreenAccent else NutriBlack)
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Camera",
                            tint = if (cameraPullFraction >= 0.75f) NutriBlack else NutriWhite,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(NutriBlack.copy(alpha = 0.85f))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (cameraPullFraction >= 0.75f) "Release to open Camera 📷" else "Swipe up to half screen for Camera",
                            color = if (cameraPullFraction >= 0.75f) NutriGreenAccent else NutriWhite,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
