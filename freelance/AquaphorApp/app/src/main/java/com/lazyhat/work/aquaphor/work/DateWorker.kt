package com.lazyhat.work.aquaphor.work

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lazyhat.work.aquaphor.MainApplication
import com.lazyhat.work.aquaphor.R
import com.lazyhat.work.aquaphor.domain.usecaseproviders.DateWorkerUseCases
import com.lazyhat.work.aquaphor.ui.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate

//Воркер для работы в фоне
@HiltWorker
class DateWorker @AssistedInject constructor(
    @Assisted ctx: Context,
    @Assisted parameters: WorkerParameters,
    private val dateWorkerUseCases: DateWorkerUseCases
) : CoroutineWorker(ctx, parameters) {
    override suspend fun doWork(): Result {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent =
            PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(applicationContext, MainApplication.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(applicationContext.getString(R.string.notification_description))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)

        val expDate = dateWorkerUseCases.getExpiresDate()
        val nowDate = LocalDate.now()
        if (nowDate == expDate) {
            Log.w("dates", "dates are equal")
            with(NotificationManagerCompat.from(applicationContext)) {
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        "android.permission.POST_NOTIFICATIONS"
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.w("Notification Permission", "DENIED")
                    return@with
                }
                notify(0, builder.build())
            }
        } else
            Log.w("dates", "dates are not equal")
        return Result.success()
    }
}