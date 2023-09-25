package com.lazyhat.work.tracker.data.network

import androidx.compose.ui.graphics.ImageBitmap

interface NetworkService {
    suspend fun sendData(
        ip: String,
        imageFront: ImageBitmap,
        imageBack: ImageBitmap,
        sendData: SendData
    )
}