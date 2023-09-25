package com.lazyhat.work.tracker.data.work

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.lazyhat.work.tracker.MainApplication
import com.lazyhat.work.tracker.Permissions
import com.lazyhat.work.tracker.R
import com.lazyhat.work.tracker.data.repo.WorkRepository
import com.lazyhat.work.tracker.ui.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.system.measureTimeMillis

@HiltWorker
class MainWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted parameters: WorkerParameters,
    private val repository: WorkRepository
) : CoroutineWorker(context, parameters) {
    private fun getBatteryStatus(): Intent? =
        IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { iFilter ->
            applicationContext.registerReceiver(null, iFilter)
        }

    private val intent = Intent(applicationContext, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    private val pendingIntent: PendingIntent =
        PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    private val notificationBuilder =
        NotificationCompat.Builder(
            applicationContext,
            MainApplication.NOTIFICATION_CHANNEL_ID
        ).apply {
            setSmallIcon(R.mipmap.ic_launcher)
            priority = NotificationCompat.PRIORITY_LOW
            setContentTitle("Tracker")
            setContentIntent(pendingIntent)
            setOngoing(true)
            setVibrate(null)
            setSilent(true)
        }

    @SuppressLint("MissingPermission")
    private fun notify(notification: Notification) {
        if (Permissions.check(Permissions.NOTIFICATIONS, applicationContext)) {
            NotificationManagerCompat.from(applicationContext).notify(0, notification)
        }
    }


    companion object {
        const val WORKER_ID = "main_worker"
        private const val PERIODIC_TAG = "periodic"
        private const val ONETIME_TAG = "onetime"

        private fun oneTimeRequest(): OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<MainWorker>()
                .addTag(ONETIME_TAG)
                .build()

        private fun periodicRequest(): PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<MainWorker>(15, TimeUnit.MINUTES)
                .addTag(PERIODIC_TAG)
                .build()

        fun WorkManager.enqueueMainWorkerOneTime() =
            enqueueUniqueWork(WORKER_ID, ExistingWorkPolicy.REPLACE, oneTimeRequest())

        fun WorkManager.enqueueMainWorkerPeriodic() =
            enqueueUniquePeriodicWork(
                WORKER_ID,
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                periodicRequest()
            )
    }


    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        repository.updateRealUpdateInterval(measureTimeMillis {

            val data = repository.appData()

            val batteryLevel = getBatteryStatus()?.let {
                val batteryLevel = it.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                repository.updateBatteryLevel(batteryLevel)
                batteryLevel
            }

            val location = repository.getLastLocation()

            repository.updateLocation(location?.latitude ?: .0, location?.longitude ?: .0)

            var backImage: ImageBitmap? = null
            var frontImage: ImageBitmap? = null

            runBlocking(Dispatchers.Main) {
                repository.updateFrontImage(suspendCoroutine {
                    repository.openFrontCamera({
                        repository.takePicture { bitmap ->
                            repository.closeCameras()
                            it.resume(bitmap)
                        }
                    })
                })
                repository.updateBackImage(suspendCoroutine {
                    repository.openBackCamera({
                        repository.takePicture { bitmap ->
                            repository.closeCameras()
                            it.resume(bitmap)
                        }
                    })
                })
            }

            val resultText =
                "battery: $batteryLevel, lat: ${location?.latitude}, long: ${location?.longitude}"

            val notification = notificationBuilder.setContentText(resultText).build()

            notify(notification)

            Log.w("WM", "${tags.first { !it.contains("MainWorker") }}, $resultText")

            delay(data.durationUnit.toMillis(data.duration))

            WorkManager.getInstance(applicationContext).enqueueMainWorkerOneTime()
        })
        return Result.success()
    }
}