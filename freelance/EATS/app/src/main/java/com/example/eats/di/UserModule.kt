package com.example.eats.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.example.eats.data.userdata.DefaultUserRepository
import com.example.eats.data.userdata.LocalUser
import com.example.eats.data.userdata.LocalUserSerializer
import com.example.eats.data.userdata.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

private const val LOCAL_USER_DATA_STORE_FILE_NAME = "local_user_ds"

@Module
@InstallIn(SingletonComponent::class)
object UserModule {
    @Singleton
    @Provides
    fun provideUserRepository(
        userDataStore: DataStore<LocalUser>
    ): UserRepository = DefaultUserRepository(userDataStore)


    @Singleton
    @Provides
    fun provideUserDataStore(@ApplicationContext context: Context): DataStore<LocalUser> =
        DataStoreFactory.create(
            serializer = LocalUserSerializer,
            produceFile = { context.dataStoreFile(LOCAL_USER_DATA_STORE_FILE_NAME) },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { LocalUser() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
}