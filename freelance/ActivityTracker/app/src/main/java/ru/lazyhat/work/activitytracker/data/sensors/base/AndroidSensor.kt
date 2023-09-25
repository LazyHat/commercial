package ru.lazyhat.work.activitytracker.data.sensors.base

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

abstract class AndroidSensor(
    private val context: Context,
    private val sensorFeature: String,
    sensorType: Int,
) : MeasurableSensor(sensorType), SensorEventListener {

    override val doesSensorExist: Boolean
        get() = context.packageManager.hasSystemFeature(sensorFeature)

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null

    override fun startListening() {
        if (!doesSensorExist)
            return
        if (!::sensorManager.isInitialized && sensor == null) {
            sensorManager = context.getSystemService(SensorManager::class.java) as SensorManager
            sensor = sensorManager.getDefaultSensor(sensorType)
        }
        sensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun stopListening() {
        if (!doesSensorExist || !::sensorManager.isInitialized)
            return
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(e: SensorEvent?) {
        if (!doesSensorExist)
            return
        if (e?.sensor?.type == sensorType)
            onSensorValuesChanged?.invoke(e.values.toList())
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) = Unit
}