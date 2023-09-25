package com.lazyhat.work.tracker.data.repo

import android.location.Location
import android.os.Handler
import androidx.compose.ui.graphics.ImageBitmap
import com.lazyhat.work.tracker.data.model.AppData

interface WorkRepository {
    suspend fun appData(): AppData
    suspend fun updateBatteryLevel(new: Int)
    suspend fun updateLocation(lat: Double, long: Double)
    suspend fun updateBackImage(bitmap: ImageBitmap)
    suspend fun updateFrontImage(bitmap: ImageBitmap)
    suspend fun updateRealUpdateInterval(new: Long)
    fun getLastLocation(): Location?
    fun openFrontCamera(opened: () -> Unit, handler: Handler? = null)
    fun openBackCamera(opened: () -> Unit, handler: Handler? = null)
    fun closeCameras()
    fun takePicture(onTaken: (ImageBitmap) -> Unit)
}