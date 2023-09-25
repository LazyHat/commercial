package ru.lazyhat.work.myhome.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import ru.lazyhat.work.myhome.data.db.tables.CameraObject
import ru.lazyhat.work.myhome.data.db.tables.DoorObject
import ru.lazyhat.work.myhome.data.network.NetworkSource
import ru.lazyhat.work.myhome.data.network.NetworkSourceInstance
import ru.lazyhat.work.myhome.data.repository.MainRepository
import ru.lazyhat.work.myhome.data.repository.MainRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideNetworkService(): NetworkSource =
        NetworkSourceInstance()

    @Provides
    @Singleton
    fun provideMainRepository(networkSource: NetworkSource, realm: Realm): MainRepository =
        MainRepositoryImpl(realm, networkSource)

    @Provides
    @Singleton
    fun provideRealm(@ApplicationContext context: Context): Realm = Realm.open(
        RealmConfiguration.create(
            setOf(CameraObject::class, DoorObject::class)
        )
    )
}