package ru.lazyhat.work.activitytracker.data.sensors

import android.content.Context
import ru.lazyhat.work.activitytracker.data.sensors.base.MeasurableSensor

data class Sensors(
    private val context: Context
) {
    val accelerometer: MeasurableSensor = AccelerometerSensor(context)
    val rotation: MeasurableSensor = RotationSensor(context)
}
