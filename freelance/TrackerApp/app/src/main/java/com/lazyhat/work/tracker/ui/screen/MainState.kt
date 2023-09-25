package com.lazyhat.work.tracker.ui.screen

import androidx.compose.ui.graphics.ImageBitmap
import java.util.concurrent.TimeUnit

data class MainState(
    val status: Boolean = false,
    val batteryLevel: Int = 0,
    val lat: Double = .0,
    val long: Double = .0,
    val savedIp: String = "",
    val currentIp: String = "",
    val duration: Long = 500,
    val durationUnit: TimeUnit = TimeUnit.MILLISECONDS,
    val imageFront: ImageBitmap? = null,
    val imageBack: ImageBitmap? = null,
    val updateInterval: Long = 0,
    val error: String? = null
)