package ru.lazyhat.work.activitytracker.data.service

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build

class ServiceController(private val context: Context) {
    fun isServiceEnabled(): Boolean {
        (context.getSystemService(ActivityManager::class.java) as ActivityManager).getRunningServices(
            Int.MAX_VALUE
        )
            .forEach {
                if (SensorService::class.java.name.equals(it.service.className))
                    return true
            }
        return false
    }

    fun stopService() {
        context.stopService(Intent(context, SensorService::class.java))
    }

    fun startService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            context.startForegroundService(Intent(context, SensorService::class.java))
        else
            context.startService(Intent(context, SensorService::class.java))
    }
}