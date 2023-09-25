package ru.lazyhat.work.activitytracker.data.sensors

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import ru.lazyhat.work.activitytracker.data.sensors.base.AndroidSensor

class RotationSensor(context: Context) :
    AndroidSensor(
        context,
        PackageManager.FEATURE_SENSOR_GYROSCOPE,
        Sensor.TYPE_ROTATION_VECTOR
    )