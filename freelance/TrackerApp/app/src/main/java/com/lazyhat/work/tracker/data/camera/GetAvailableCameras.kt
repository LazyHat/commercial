package com.lazyhat.work.tracker.data.camera

import android.graphics.ImageFormat
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.util.Log

private const val CAMERA_INFO_LOG_TAG = "CAMERA_INFO"

fun getAvailableCameras(cameraManager: CameraManager): CameraHolder {
    val builder = CameraHolder.Builder(cameraManager)
    cameraManager.cameraIdList.forEach { id ->
        Log.i(CAMERA_INFO_LOG_TAG, "----- CameraID: $id -----")

        val cc = cameraManager.getCameraCharacteristics(id.toString())

        val facing = cc.get(CameraCharacteristics.LENS_FACING)

        Log.i(
            CAMERA_INFO_LOG_TAG, "Facing: ${

                when (facing) {
                    CameraCharacteristics.LENS_FACING_BACK -> "Back"

                    CameraCharacteristics.LENS_FACING_FRONT -> "Front"

                    else -> "Unknown"
                }

            }"
        )

        val confMap = cc.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

        if (confMap != null && confMap.isOutputSupportedFor(ImageFormat.JPEG)) {

            val jpegSizes = confMap.getOutputSizes(ImageFormat.JPEG)

            if (jpegSizes.isNotEmpty()) {

                Log.i(CAMERA_INFO_LOG_TAG, "Supported JPEG sizes: ")

                jpegSizes.forEachIndexed { index, size ->

                    Log.i(
                        CAMERA_INFO_LOG_TAG,
                        "   $index. h: ${size.height}, w: ${size.width}"
                    )

                }
            }
        } else {
            Log.i(CAMERA_INFO_LOG_TAG, "Camera don't support JPEG")
        }
        when (facing) {
            CameraCharacteristics.LENS_FACING_FRONT -> builder.addFrontCamera(
                id.toString()
            )

            CameraCharacteristics.LENS_FACING_BACK -> builder.addBackCamera(
                id.toString()
            )

            else -> TODO("Unknown camera")
        }
    }
    return builder.build()
}