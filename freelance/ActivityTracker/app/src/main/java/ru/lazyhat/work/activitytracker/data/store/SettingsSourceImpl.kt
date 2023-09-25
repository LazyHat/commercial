package ru.lazyhat.work.activitytracker.data.store

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import ru.lazyhat.work.activitytracker.data.models.AppSettings

class SettingsSourceImpl(
    private val dataStore: DataStore<AppSettings>,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) : SettingsSource {
    override suspend fun update(new: (current: AppSettings) -> AppSettings) {
        withContext(coroutineDispatcher) {
            dataStore.updateData { new(it) }
        }
    }

    override suspend fun get(): AppSettings =
        withContext(coroutineDispatcher) { dataStore.data.first() }

    override val flow: Flow<AppSettings> = dataStore.data
}