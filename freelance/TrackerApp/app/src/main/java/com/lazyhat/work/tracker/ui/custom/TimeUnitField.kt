package com.lazyhat.work.tracker.ui.custom

import androidx.compose.runtime.Composable
import java.util.concurrent.TimeUnit

@Composable
fun TimeUnitField(onChange: (millis: Long) -> Unit) {
    TimeUnit.SECONDS.toMillis(2)
}