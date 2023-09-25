package com.lazyhat.work.aquaphor

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.lazyhat.work.aquaphor.work.DateWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

//Приложение самый главный класс
@HiltAndroidApp
class MainApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder().setWorkerFactory(workerFactory).build()
    }

    override fun onCreate() {
        super.onCreate()

        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val work = PeriodicWorkRequestBuilder<DateWorker>(1, TimeUnit.DAYS).build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "notificator", ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, work
        )
    }

    companion object {
        val CHANNEL_ID = "notification_id"
    }
}