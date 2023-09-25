package com.lazyhat.work.tracker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

enum class Permissions(val string: String) {
    FINE_LOCATION(Manifest.permission.ACCESS_FINE_LOCATION),
    COARSE_LOCATION(Manifest.permission.ACCESS_COARSE_LOCATION),
    BACK_LOCATION(Manifest.permission.ACCESS_COARSE_LOCATION),
    NOTIFICATIONS(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.POST_NOTIFICATIONS
        else
            Manifest.permission.ACCESS_NOTIFICATION_POLICY
    ),
    CAMERA(Manifest.permission.CAMERA);

    companion object {
        fun getAll(): Array<String> =
            Permissions.values().map { it.string }.toTypedArray()

        fun getAllIsGranted(context: Context): Array<Boolean> =
            Permissions.values().map {
                context.checkSelfPermission(it.string) == PackageManager.PERMISSION_GRANTED
            }.toTypedArray()

        fun getDenied(context: Context): Array<String> = values().map { it.string }.toTypedArray()

        fun check(permission: Permissions, context: Context): Boolean =
            context.checkSelfPermission(permission.string) == PackageManager.PERMISSION_GRANTED

        fun checkGps(context: Context) =
            check(FINE_LOCATION, context) && check(COARSE_LOCATION, context) && check(
                BACK_LOCATION,
                context
            )
    }
}