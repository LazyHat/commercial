package com.lazyhat.work.tracker.di

import android.content.Context
import android.hardware.camera2.CameraManager
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.work.WorkManager
import com.lazyhat.work.tracker.data.camera.CameraHolder
import com.lazyhat.work.tracker.data.camera.getAvailableCameras
import com.lazyhat.work.tracker.data.location.LocationService
import com.lazyhat.work.tracker.data.location.LocationServiceImpl
import com.lazyhat.work.tracker.data.model.AppData
import com.lazyhat.work.tracker.data.network.NetworkService
import com.lazyhat.work.tracker.data.network.NetworkServiceImpl
import com.lazyhat.work.tracker.data.repo.MainRepository
import com.lazyhat.work.tracker.data.repo.MainRepositoryImpl
import com.lazyhat.work.tracker.data.repo.WorkRepository
import com.lazyhat.work.tracker.data.repo.WorkRepositoryImpl
import com.lazyhat.work.tracker.data.store.AppDataSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Singleton
    @Provides
    fun provideAppDataDataStore(@ApplicationContext context: Context): DataStore<AppData> =
        DataStoreFactory.create(
            serializer = AppDataSerializer,
            produceFile = { context.dataStoreFile("app_settings") },
            corruptionHandler = ReplaceFileCorruptionHandler { AppData() },
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )

    @Singleton
    @Provides
    fun provideLocationService(
        @ApplicationContext context: Context
    ): LocationService =
        LocationServiceImpl(
            context
        )

    @Singleton
    @Provides
    fun provideCameraHolder(
        @ApplicationContext context: Context
    ): CameraHolder =
        getAvailableCameras(context.getSystemService(Context.CAMERA_SERVICE) as CameraManager)

    @Singleton
    @Provides
    fun provideWorkRepository(
        appDataStore: DataStore<AppData>,
        locationService: LocationService,
        workManager: WorkManager,
        cameraHolder: CameraHolder
    ): WorkRepository =
        WorkRepositoryImpl(appDataStore, locationService, workManager, cameraHolder)

    @Singleton
    @Provides
    fun provideMainRepository(
        appDataStore: DataStore<AppData>,
        workManager: WorkManager
    ): MainRepository =
        MainRepositoryImpl(appDataStore, workManager)

    @Singleton
    @Provides
    fun provideWorkManager(@ApplicationContext context: Context) = WorkManager.getInstance(context)

    @Singleton
    @Provides
    fun provideNetworkService(): NetworkService = NetworkServiceImpl(HttpClient(Android) {
        install(ContentNegotiation) {
            json()
        }
    })
}