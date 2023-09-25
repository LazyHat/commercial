package ru.lazyhat.work.activitytracker.data.models

import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

//time in seconds

@Serializable
data class AppSettings(
    val accuracyAccelerometer: Float,
    val sensorUpdateRate: Duration,
    val allowedTimeOfImmobility: Duration,
    val messageRepeatingRate: Duration,
    val timeOfImmobility: Duration,
    val timeOfLastSMS: Duration,
    val contacts: Set<Contact>,
    val previousValues: List<Float>
) {
    companion object {
        val Default =
            AppSettings(
                0.2f,
                5.seconds,
                10.hours,
                10.minutes,
                Duration.ZERO,
                Duration.INFINITE,
                setOf(),
                listOf()
            )
    }
}