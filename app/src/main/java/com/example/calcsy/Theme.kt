package com.example.calcsy

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily

data class CalculatorColors(
    val screenBackground: Color,
    val displayBackground: Color,
    val displayExpressionText: Color,
    val displayResultText: Color,
    val numberButtonBg: Color,
    val numberButtonText: Color,
    val operatorButtonBg: Color,
    val operatorButtonText: Color,
    val equalsButtonBg: Color,
    val equalsButtonText: Color,
    val actionButtonBg: Color,
    val actionButtonText: Color
)

val DarkCalculatorColors = CalculatorColors(
    screenBackground = Color(0xFF0F1115),
    displayBackground = Color(0xFF171A21),
    displayExpressionText = Color(0xFF8A93A6),
    displayResultText = Color(0xFFF2F4F8),
    numberButtonBg = Color(0xFF232832),
    numberButtonText = Color(0xFFF2F4F8),
    operatorButtonBg = Color(0xFF2F3A4A),
    operatorButtonText = Color(0xFF4FD1C5),
    equalsButtonBg = Color(0xFF4FD1C5),
    equalsButtonText = Color(0xFF0F1115),
    actionButtonBg = Color(0xFF2A2328),
    actionButtonText = Color(0xFFFF7A7A)
)

val LightCalculatorColors = CalculatorColors(
    screenBackground = Color(0xFFF5F7FA),
    displayBackground = Color(0xFFFFFFFF),
    displayExpressionText = Color(0xFF6B7385),
    displayResultText = Color(0xFF1A1D24),
    numberButtonBg = Color(0xFFFFFFFF),
    numberButtonText = Color(0xFF1A1D24),
    operatorButtonBg = Color(0xFFE3F6F4),
    operatorButtonText = Color(0xFF0E8F86),
    equalsButtonBg = Color(0xFF0E8F86),
    equalsButtonText = Color(0xFFFFFFFF),
    actionButtonBg = Color(0xFFFDECEC),
    actionButtonText = Color(0xFFD64545)
)

val LocalCalculatorColors = staticCompositionLocalOf { DarkCalculatorColors }

val TabularTextStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontFeatureSettings = "tnum"
)

@Composable
fun CalcsyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkCalculatorColors else LightCalculatorColors
    val materialColorScheme = if (darkTheme) {
        darkColorScheme(
            background = colors.screenBackground,
            surface = colors.displayBackground
        )
    } else {
        lightColorScheme(
            background = colors.screenBackground,
            surface = colors.displayBackground
        )
    }

    CompositionLocalProvider(LocalCalculatorColors provides colors) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            content = content
        )
    }
}
