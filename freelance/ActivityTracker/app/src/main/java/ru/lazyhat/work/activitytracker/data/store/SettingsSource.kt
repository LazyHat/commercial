package ru.lazyhat.work.activitytracker.data.store

import kotlinx.coroutines.flow.Flow
import ru.lazyhat.work.activitytracker.data.models.AppSettings

interface SettingsSource {
    suspend fun update(new: (current: AppSettings) -> AppSettings)
    val flow : Flow<AppSettings>
    suspend fun get(): AppSettings
}