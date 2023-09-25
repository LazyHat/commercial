package com.lazyhat.work.tracker.data.camera

import androidx.compose.ui.graphics.ImageBitmap
import android.os.Handler

interface CameraService {
    fun isOpen(): Boolean
    fun open(opened: (() -> Unit)?, handler: Handler?)
    fun takePicture(onTaken: (ImageBitmap) -> Unit)
    fun close()
}