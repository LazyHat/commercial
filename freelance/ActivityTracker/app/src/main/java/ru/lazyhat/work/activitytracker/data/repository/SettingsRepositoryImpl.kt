package ru.lazyhat.work.activitytracker.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.lazyhat.work.activitytracker.data.models.AppSettings
import ru.lazyhat.work.activitytracker.data.models.Contact
import ru.lazyhat.work.activitytracker.data.store.SettingsSource
import kotlin.time.Duration

class SettingsRepositoryImpl(
    private val settingsSource: SettingsSource,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) : SettingsRepository {
    override val settingsFlow: Flow<AppSettings> = settingsSource.flow
    override suspend fun updatePreviousValues(new: List<Float>) {
        withContext(coroutineDispatcher) {
            settingsSource.update { it.copy(previousValues = new) }
        }
    }

    override suspend fun increaseTimeOfLastSMS(plus: Duration) {
        withContext(coroutineDispatcher) {
            settingsSource.update { it.copy(timeOfLastSMS = it.timeOfLastSMS.plus(plus)) }
        }
    }

    override suspend fun resetTimeLastSMS() {
        withContext(coroutineDispatcher) {
            settingsSource.update { it.copy(timeOfLastSMS = Duration.ZERO) }
        }
    }

    override suspend fun stopTimeLastSMS() {
        withContext(coroutineDispatcher) {
            settingsSource.update { it.copy(timeOfLastSMS = Duration.INFINITE) }
        }
    }

    override suspend fun updateAccuracyAccelerometer(new: Float) {
        withContext(coroutineDispatcher) {
            settingsSource.update { it.copy(accuracyAccelerometer = new) }
        }
    }

    override suspend fun updateSensorUpdateRate(new: Duration) {
        withContext(coroutineDispatcher) {
            settingsSource.update { it.copy(sensorUpdateRate = new) }
        }
    }

    override suspend fun updateAllowedTimeOfImmobility(new: Duration) {
        withContext(coroutineDispatcher) {
            settingsSource.update { it.copy(allowedTimeOfImmobility = new) }
        }
    }

    override suspend fun updateMessageRepeatingRate(new: Duration) {
        withContext(coroutineDispatcher) {
            settingsSource.update { it.copy(messageRepeatingRate = new) }
        }
    }

    override suspend fun addNewContact(new: Contact) {
        if (settingsSource.get().contacts.size >= 3)
            return
        withContext(coroutineDispatcher) {
            settingsSource.update { it.copy(contacts = it.contacts.plusElement(new)) }
        }
    }

    override suspend fun deleteContact(index: Int) {
        withContext(coroutineDispatcher) {
            settingsSource.update {
                it.copy(contacts = it.contacts.filterIndexed { idx, _ -> idx != index }.toSet())
            }
        }
    }

    override suspend fun increaseTimeOfImmobility(plus: Duration) {
        withContext(coroutineDispatcher) {
            settingsSource.update {
                it.copy(timeOfImmobility = it.timeOfImmobility.plus(plus))
            }
        }
    }

    override suspend fun resetTimeOfImmobility() {
        withContext(coroutineDispatcher) {
            settingsSource.update {
                it.copy(timeOfImmobility = Duration.ZERO)
            }
        }
    }
}