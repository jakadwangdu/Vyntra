package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NutriWhite,
    onPrimary = NutriBlack,
    primaryContainer = NutriDarkGray,
    onPrimaryContainer = NutriWhite,
    secondary = NutriGreenAccent,
    onSecondary = NutriWhite,
    background = NutriBlack,
    onBackground = NutriWhite,
    surface = Color(0xFF222320),
    onSurface = NutriWhite,
    surfaceVariant = Color(0xFF2C2D29),
    onSurfaceVariant = Color(0xFFD4D4D8),
    outline = Color(0xFF3F3F46)
)

private val LightColorScheme = lightColorScheme(
    primary = NutriBlack,
    onPrimary = NutriWhite,
    primaryContainer = NutriBlack,
    onPrimaryContainer = NutriWhite,
    secondary = NutriGreenAccent,
    onSecondary = NutriWhite,
    background = NutriBg,
    onBackground = NutriBlack,
    surface = NutriWhite,
    onSurface = NutriBlack,
    surfaceVariant = NutriCardBg,
    onSurfaceVariant = NutriDarkGray,
    outline = NutriBorder
)

@Composable
fun NutriLensTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
