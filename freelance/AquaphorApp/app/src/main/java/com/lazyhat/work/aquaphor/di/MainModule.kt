package com.lazyhat.work.aquaphor.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.lazyhat.work.aquaphor.data.datastore.FilterDataSerializer
import com.lazyhat.work.aquaphor.data.models.FilterData
import com.lazyhat.work.aquaphor.data.repository.FilterRepository
import com.lazyhat.work.aquaphor.data.repository.FilterRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

//Главные зависимости, репозиторий и хранилище
@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Singleton
    @Provides
    fun provideFilterRepository(dataStore: DataStore<FilterData>): FilterRepository =
        FilterRepositoryImpl(dataStore)

    @Singleton
    @Provides
    fun provideFilterDataStore(@ApplicationContext ctx: Context): DataStore<FilterData> =
        DataStoreFactory.create(
            serializer = FilterDataSerializer,
            produceFile = { ctx.dataStoreFile("filter") },
            corruptionHandler = ReplaceFileCorruptionHandler(produceNewData = { FilterData.Empty }),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
}