package ru.lazyhat.work.activitytracker.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import ru.lazyhat.work.activitytracker.data.contacts.ContactsService
import ru.lazyhat.work.activitytracker.data.contacts.ContactsServiceImpl
import ru.lazyhat.work.activitytracker.data.models.AppSettings
import ru.lazyhat.work.activitytracker.data.repository.SettingsRepository
import ru.lazyhat.work.activitytracker.data.repository.SettingsRepositoryImpl
import ru.lazyhat.work.activitytracker.data.sensors.Sensors
import ru.lazyhat.work.activitytracker.data.service.ServiceController
import ru.lazyhat.work.activitytracker.data.store.AppSettingsSerializer
import ru.lazyhat.work.activitytracker.data.store.SettingsSource
import ru.lazyhat.work.activitytracker.data.store.SettingsSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Singleton
    @Provides
    fun provideServiceEnabledChecker(@ApplicationContext context: Context): ServiceController =
        ServiceController(context)

    @Singleton
    @Provides
    fun provideContactService(@ApplicationContext context: Context): ContactsService =
        ContactsServiceImpl(context)

    @Singleton
    @Provides
    fun provideSettingsRepository(settingsSource: SettingsSource): SettingsRepository =
        SettingsRepositoryImpl(settingsSource)

    @Singleton
    @Provides
    fun provideSettingSource(dataStore: DataStore<AppSettings>): SettingsSource =
        SettingsSourceImpl(dataStore)

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context) = DataStoreFactory.create(
        serializer = AppSettingsSerializer,
        produceFile = { context.dataStoreFile("app_settings") },
        corruptionHandler = ReplaceFileCorruptionHandler { AppSettings.Default },
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    )

    @Singleton
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideSensors(@ApplicationContext context: Context): Sensors =
        Sensors(context)
}