package ru.lazyhat.work.activitytracker.data.sensors

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import ru.lazyhat.work.activitytracker.data.sensors.base.AndroidSensor

class AccelerometerSensor(context: Context) :
    AndroidSensor(context, PackageManager.FEATURE_SENSOR_ACCELEROMETER, Sensor.TYPE_ACCELEROMETER) {
}