package ru.lazyhat.work.nutriguide.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = Green,
    surface = Color.Gray,
    background = Color.White,
    primaryContainer = Color.Gray
)

private val NutriGuideShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(5.dp),
    extraSmall = RoundedCornerShape(3.dp),
    large = RoundedCornerShape(6.dp),
    extraLarge = RoundedCornerShape(7.dp)
)

@Composable
fun NutriGuideTheme(
    content: @Composable () -> Unit
) {
    val scheme = LightColorScheme
    MaterialTheme(
        colorScheme = scheme,
        typography = Typography,
        shapes = NutriGuideShapes,
        content = content
    )
}