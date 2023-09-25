package ru.lazyhat.work.activitytracker.data.repository

import kotlinx.coroutines.flow.Flow
import ru.lazyhat.work.activitytracker.data.models.AppSettings
import ru.lazyhat.work.activitytracker.data.models.Contact
import kotlin.time.Duration

interface SettingsRepository {
    val settingsFlow: Flow<AppSettings>
    suspend fun updateAccuracyAccelerometer(new: Float)
    suspend fun updateSensorUpdateRate(new: Duration)
    suspend fun updateAllowedTimeOfImmobility(new: Duration)
    suspend fun updateMessageRepeatingRate(new: Duration)
    suspend fun addNewContact(new: Contact)
    suspend fun deleteContact(index: Int)
    suspend fun increaseTimeOfImmobility(plus: Duration)
    suspend fun resetTimeOfImmobility()
    suspend fun updatePreviousValues(new: List<Float>)
    suspend fun resetTimeLastSMS()
    suspend fun increaseTimeOfLastSMS(plus: Duration)
    suspend fun stopTimeLastSMS()
}