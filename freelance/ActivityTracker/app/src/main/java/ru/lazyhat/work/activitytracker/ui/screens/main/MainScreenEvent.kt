package ru.lazyhat.work.activitytracker.ui.screens.main

import ru.lazyhat.work.activitytracker.data.models.Contact
import kotlin.time.Duration

sealed class MainScreenEvent {
    data class UpdateAccuracyAccelerometer(val new: Float) : MainScreenEvent()
    data class UpdateSensorUpdateRate(val new: Duration) : MainScreenEvent()
    data class UpdateAllowedTimeOfImmobility(val new: Duration) : MainScreenEvent()
    data class UpdateMessageRepeatingRate(val new: Duration) : MainScreenEvent()
    data class DeleteContact(val index: Int) : MainScreenEvent()
    data class AddNewContact(val new: Contact) : MainScreenEvent()
    object OpenAlertDialog : MainScreenEvent()
    object CloseAlertDialog : MainScreenEvent()
    object StartService : MainScreenEvent()
    object StopService : MainScreenEvent()
}