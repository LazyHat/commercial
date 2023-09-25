package ru.lazyhat.work.activitytracker.ui.screens.main

import ru.lazyhat.work.activitytracker.data.models.AppSettings
import ru.lazyhat.work.activitytracker.data.models.Contact
import kotlin.time.Duration

data class MainScreenState(
    val isAlertDialogOpened: Boolean,
    val accuracyAccelerometer: TextFieldState<Float>,
    val sensorUpdateRate: TextFieldState<Duration>,
    val allowedTimeOfImmobility: TextFieldState<Duration>,
    val messageRepeatingRate: TextFieldState<Duration>,
    val timeOfImmobility: Duration,
    val savedContacts: Set<Contact>,
    val serviceEnabled: Boolean
) {
    companion object {
        val Default = AppSettings.Default.let {
            MainScreenState(
                false,
                TextFieldState.default(AppSettings.Default.accuracyAccelerometer),
                TextFieldState.default(AppSettings.Default.sensorUpdateRate),
                TextFieldState.default(AppSettings.Default.allowedTimeOfImmobility),
                TextFieldState.default(AppSettings.Default.messageRepeatingRate),
                AppSettings.Default.timeOfImmobility,
                setOf(),
                false
            )
        }
    }
}

data class TextFieldState<T>(
    val value: T,
    val error: String?
) {
    companion object {
        fun <T> default(defaultValue: T) = TextFieldState(defaultValue, null)
    }
}