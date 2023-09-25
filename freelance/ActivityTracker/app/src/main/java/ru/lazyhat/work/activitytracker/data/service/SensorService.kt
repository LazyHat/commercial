package ru.lazyhat.work.activitytracker.data.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.telephony.SmsManager
import android.util.Log
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingCommand
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import ru.lazyhat.work.activitytracker.R
import ru.lazyhat.work.activitytracker.data.models.AppSettings
import ru.lazyhat.work.activitytracker.data.repository.SettingsRepository
import ru.lazyhat.work.activitytracker.data.sensors.Sensors
import javax.inject.Inject
import kotlin.math.abs
import kotlin.time.Duration

private const val contentText = "Служба слежения за неподвижностью активна"
private const val contentTitle = "Служба активна"
private val smallIcon = R.drawable.ic_channel_sensor_foreground

@AndroidEntryPoint
class SensorService : Service() {
    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var sensors: Sensors

    override fun onBind(intent: Intent): IBinder {
        Log.w("SensorService", "onBind")
        return Binder()
    }

    override fun onCreate() {
        Log.w("SensorService", "onCreate")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            (getSystemService(NotificationManager::class.java) as NotificationManager).createNotificationChannel(
                NotificationChannel(
                    channelId,
                    "Служба опрашивания сенсоров",
                    NotificationManager.IMPORTANCE_LOW
                )
            )
        super.onCreate()
    }

    private val channelId = "SensorForegroundServiceChannel"

    @OptIn(DelicateCoroutinesApi::class)
    private val coroutineContext = newSingleThreadContext("service-thread")

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val scope = CoroutineScope(coroutineContext)

        Log.w("SensorService", "onStartCommand")

        val notification =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                Notification.Builder(applicationContext, channelId)
                    .setContentText(contentText)
                    .setSmallIcon(smallIcon)
                    .setContentTitle(contentTitle)
                    .build()
            else
                NotificationCompat.Builder(applicationContext, channelId)
                    .setContentText(contentText)
                    .setSmallIcon(smallIcon)
                    .setContentTitle(contentTitle)
                    .build()

        startForeground(1, notification)

        scope.launch(coroutineContext) {
            Log.w("ServiceWork", "Started")

            val accelerometerSensorValuesFlow = sensors.accelerometer.getValuesFlow().stateIn(
                this, {
                    it.map {
                        if (isActive)
                            SharingCommand.START
                        else
                            SharingCommand.STOP
                    }
                }, listOf(0f, 0f, 0f)
            )

            val settingsFlow = settingsRepository.settingsFlow.stateIn(
                this,
                {
                    it.map {
                        if (isActive)
                            SharingCommand.START
                        else
                            SharingCommand.STOP
                    }
                },
                AppSettings.Default
            )

            while (true) {
                mainWork(settingsFlow, accelerometerSensorValuesFlow)
                delay(settingsFlow.first().sensorUpdateRate.inWholeMilliseconds)
            }

        }.invokeOnCompletion {
            if (it != null)
                Log.w("ServiceWork", "Work Canceled")
            else
                Log.w("ServiceWork", "Work successfully finished")
            CoroutineScope(Dispatchers.IO).launch {
                settingsRepository.resetTimeOfImmobility()
                settingsRepository.stopTimeLastSMS()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.w("SensorService", "onDestroy")
        coroutineContext.close()
        super.onDestroy()
    }

    private suspend fun mainWork(
        settingsFlow: StateFlow<AppSettings>,
        accelerometerSensorValuesFlow: StateFlow<List<Float>>
    ) {
        val settings = settingsFlow.first()
        val accelerometerValues = accelerometerSensorValuesFlow.first()

        if (settings.previousValues.isEmpty() || accelerometerValues.foldIndexed(false) { index, acc, it ->
                acc || (abs(it - settings.previousValues[index]) >=
                        settings.accuracyAccelerometer)
            }) {
            settingsRepository.resetTimeOfImmobility()
            settingsRepository.updatePreviousValues(accelerometerValues)
        }
        settingsRepository.increaseTimeOfImmobility(settings.sensorUpdateRate)

        if (settings.timeOfImmobility >= settings.allowedTimeOfImmobility) {
            if (settings.timeOfLastSMS >= settings.messageRepeatingRate) {
                Log.w("ServiceWork", "Send message to phones${settings.contacts}")
                try {
                    val smsManager =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                            (applicationContext.getSystemService(SmsManager::class.java) as SmsManager).createForSubscriptionId(
                                SmsManager.getDefaultSmsSubscriptionId()
                            )
                        else
                            SmsManager.getSmsManagerForSubscriptionId(SmsManager.getDefaultSmsSubscriptionId())

                    settings.contacts.forEach {
                        smsManager.sendTextMessage(
                            it.phone,
                            null,
                            "Телефон находится в неподвижном состоянии более ${settings.timeOfImmobility}",
                            null,
                            null
                        )
                    }
                } catch (e: Exception) {
                    Log.w("SensorServiceWork", e.message.orEmpty())
                }
                settingsRepository.resetTimeLastSMS()
            } else {
                settingsRepository.increaseTimeOfLastSMS(settings.sensorUpdateRate)
            }
        } else if (settings.timeOfLastSMS != Duration.INFINITE) {
            settingsRepository.stopTimeLastSMS()
        }
        Log.w(
            "ServiceWork",
            "Work finishPoint, ${settingsFlow.first().timeOfImmobility}, ${settingsFlow.first().timeOfLastSMS}"
        )
    }
}
