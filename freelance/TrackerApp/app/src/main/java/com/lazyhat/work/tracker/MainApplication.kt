package com.lazyhat.work.tracker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.lazyhat.work.tracker.data.repo.MainRepository
import com.lazyhat.work.tracker.data.work.MainWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var mainRepository: MainRepository

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder().setWorkerFactory(workerFactory).build()
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "work_notification"
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "App data send"
            val descriptionText = "Notification while app sends data"
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            mChannel.enableVibration(false)

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}