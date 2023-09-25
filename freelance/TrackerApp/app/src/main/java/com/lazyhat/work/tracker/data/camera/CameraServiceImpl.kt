package com.lazyhat.work.tracker.data.camera

import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.media.ImageReader
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap


class CameraServiceImpl(private val cameraManager: CameraManager, private val cameraID: String) :
    CameraService {

    private var cameraDevice: CameraDevice? = null

    private var cameraCaptureSession: CameraCaptureSession? = null

    private var imageReader =
        (cameraManager.getCameraCharacteristics(cameraID)
            .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

            ?: throw Exception("CAMERA $cameraID NULL OUTPUT FORMATS")).let { map ->

            map.getOutputSizes(ImageFormat.JPEG).maxBy { it.width }

        }.let {
            ImageReader.newInstance(it.width, it.height, ImageFormat.JPEG, 1)
        }

    override fun isOpen(): Boolean = cameraDevice != null

    override fun open(opened: (() -> Unit)?, handler: Handler?) {
        try {
            cameraManager.openCamera(cameraID, object : CameraDevice.StateCallback() {

                override fun onOpened(camera: CameraDevice) {
                    Log.i("CAMERA_SERVICE", "Camera $cameraID has opened")

                    cameraDevice = camera

                    try {
                        @Suppress("DEPRECATION")
                        cameraDevice!!.createCaptureSession(
                            listOf(imageReader.surface),

                            object : CameraCaptureSession.StateCallback() {
                                override fun onConfigured(session: CameraCaptureSession) {
                                    cameraCaptureSession = session
                                    opened?.invoke()
                                }

                                override fun onConfigureFailed(session: CameraCaptureSession) {
                                    Log.e("CAMERA_SERVICE", "CaptureSessionError: configure error")
                                }
                            },
                            handler
                        )

                    } catch (e: CameraAccessException) {
                        Log.e("CAMERA_SERVICE", "CaptureSessionFail: CameraAccessError")
                        e.printStackTrace()
                    }

                }

                override fun onDisconnected(camera: CameraDevice) {
                    Log.i("CAMERA_SERVICE", "Camera $cameraID has disconnected")
                }

                override fun onError(camera: CameraDevice, errorCode: Int) {
                    Log.e("CAMERA_SERVICE", "Camera id: $cameraID errorCode: $errorCode")
                }

            }, null)

        } catch (e: SecurityException) {
            Log.e("CAMERA_ERROR", e.message.toString())
        } catch (e: CameraAccessException) {
            Log.e("CAMERA_ERROR", e.message.toString())
        }
    }

    override fun takePicture(onTaken: (ImageBitmap) -> Unit) {
        imageReader.setOnImageAvailableListener({ reader ->
            val image = reader.acquireNextImage()
            val buffer = image.planes[0].buffer
            val bytes = ByteArray(buffer.capacity()) {
                buffer.get(it)
            }

            onTaken(
                BitmapFactory.decodeByteArray(
                    bytes,
                    0,
                    bytes.size,
                    null
                ).asImageBitmap()
            )
            image.close()
        }, null)

        try {
            val captureBuilder =
                cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE).apply {
                    addTarget(imageReader.surface)
                }
            cameraCaptureSession?.capture(
                captureBuilder.build(),
                object : CameraCaptureSession.CaptureCallback() {}, null
            ) ?: throw Exception("CameraCaptureSession is null")

        } catch (e: CameraAccessException) {
            Log.e("CAMERA_SERVICE", "TakePictureError: ${e.message}")
        }
    }

    override fun close() {
        if (cameraDevice != null) {
            cameraDevice!!.close()
            cameraDevice = null
        }
    }
}