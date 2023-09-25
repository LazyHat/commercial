package com.lazyhat.work.tracker.data.camera

import android.hardware.camera2.CameraManager
import android.os.Handler
import androidx.compose.ui.graphics.ImageBitmap
import java.io.InvalidObjectException

class CameraHolder private constructor(
    private val backCamera: CameraService,
    private val frontCamera: CameraService
) {
    fun openFront(opened: () -> Unit, handler: Handler?) = frontCamera.open(opened, handler)
    fun openBack(opened: () -> Unit, handler: Handler?) = backCamera.open(opened, handler)

    fun takePicture(onTaken: (ImageBitmap) -> Unit) = when {
        frontCamera.isOpen() -> frontCamera.takePicture(onTaken)
        backCamera.isOpen() -> backCamera.takePicture(onTaken)
        else -> throw Exception("all cameras are closed")
    }

    fun closeCameras() {
        frontCamera.close()
        backCamera.close()
    }

    class Builder(private val cameraManager: CameraManager) {
        private var _backCamera: CameraService? = null
        private var _frontCamera: CameraService? = null

        fun addBackCamera(id: String): Builder = this.apply {
            _backCamera = CameraServiceImpl(cameraManager, id)
        }

        fun addFrontCamera(id: String): Builder = this.apply {
            _frontCamera = CameraServiceImpl(cameraManager, id)
        }

        fun build(): CameraHolder = CameraHolder(
            _backCamera ?: throw InvalidObjectException("Back camera has not been added"),
            _frontCamera ?: throw InvalidObjectException("Front camera has not been added")
        )
    }
}