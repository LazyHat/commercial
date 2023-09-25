package ru.lazyhat.work.myhome.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val StarColor = Color(0xFFE0BE35)
val RecColor = Color(0xFFFA3030)

@Composable
fun MyHomeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color.White,
            primaryContainer = Color.White,
            onPrimaryContainer = Color.Black,
            onSecondaryContainer = Color.Black,
            secondary = Color(0xFF03A9F4),
            background = Color(0xFFF6F6F6)
        ),
        content = content
    )
}