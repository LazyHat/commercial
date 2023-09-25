package com.lazyhat.work.tracker.data.repo

import android.annotation.SuppressLint
import android.location.Location
import android.os.Handler
import androidx.compose.ui.graphics.ImageBitmap
import androidx.datastore.core.DataStore
import androidx.work.WorkManager
import com.lazyhat.work.tracker.data.camera.CameraHolder
import com.lazyhat.work.tracker.data.location.LocationService
import com.lazyhat.work.tracker.data.model.AppData
import com.lazyhat.work.tracker.data.model.ByteImage
import kotlinx.coroutines.flow.first

@SuppressLint("MissingPermission")
class WorkRepositoryImpl(
    private val appDataStore: DataStore<AppData>,
    private val locationService: LocationService,
    private val workManager: WorkManager,
    private val cameraHolder: CameraHolder
) : WorkRepository {

    override fun getLastLocation(): Location? = locationService.location
    override suspend fun updateLocation(lat: Double, long: Double) {
        appDataStore.updateData { it.copy(lat = lat, long = long) }
    }

    override suspend fun appData(): AppData = appDataStore.data.first()

    override suspend fun updateBatteryLevel(new: Int) {
        appDataStore.updateData { it.copy(batteryLevel = new) }
    }

    override suspend fun updateRealUpdateInterval(new: Long) {
        appDataStore.updateData { it.copy(realUpdateInterval = new) }
    }

    override suspend fun updateBackImage(bitmap: ImageBitmap) {
        appDataStore.updateData { it.copy(pictureBack = ByteImage(bitmap)) }
    }

    override suspend fun updateFrontImage(bitmap: ImageBitmap) {
        appDataStore.updateData { it.copy(pictureFront = ByteImage(bitmap)) }
    }

    override fun openFrontCamera(opened: () -> Unit, handler: Handler?) =
        cameraHolder.openFront(opened, handler)

    override fun openBackCamera(opened: () -> Unit, handler: Handler?) =
        cameraHolder.openBack(opened, handler)

    override fun takePicture(onTaken: (ImageBitmap) -> Unit) = cameraHolder.takePicture(onTaken)

    override fun closeCameras() = cameraHolder.closeCameras()
}