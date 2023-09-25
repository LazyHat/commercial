package ru.lazyhat.work.activitytracker.ui.screens

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale

@Composable
fun LoadingScreen() {
    val transition = rememberInfiniteTransition()
    val rotation by transition.animateFloat(
        initialValue = 0f, targetValue = 360f, animationSpec = InfiniteRepeatableSpec(
            tween(1000, 0, LinearEasing),
            RepeatMode.Restart,
            initialStartOffset = StartOffset(0)
        )
    )
    Box(Modifier.fillMaxSize()) {
        Icon(
            Icons.Default.Refresh, null, modifier = Modifier
                .align(Alignment.Center)
                .rotate(rotation)
                .scale(2f)
        )
    }
}