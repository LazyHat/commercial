package com.lazyhat.work.tracker.data.model

import kotlinx.serialization.Serializable
import java.util.concurrent.TimeUnit

@Serializable
data class AppData(
    val ip: IpAddress = IpAddress.Zero,
    val batteryLevel: Int = 0,
    val lat: Double = .0,
    val long: Double = .0,
    val duration: Long = 500,
    val durationUnit: TimeUnit = TimeUnit.MILLISECONDS,
    val pictureBack: ByteImage? = null,
    val pictureFront: ByteImage? = null,
    val realUpdateInterval: Long = 0
)