package com.lazyhat.work.tracker.data.network

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import java.io.ByteArrayOutputStream

class NetworkServiceImpl(private val client: HttpClient) : NetworkService {
    override suspend fun sendData(
        ip: String,
        imageFront: ImageBitmap,
        imageBack: ImageBitmap,
        sendData: SendData
    ) {
        client.post {
            url { host = ip }
            contentType(ContentType.Application.Json)
            setBody(sendData)
        }
        client.post {
            url { host = ip }
            contentType(ContentType.Image.JPEG)
            setBody(imageBack.asAndroidBitmap().let {
                val stream = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.toByteArray()
            })
        }
        client.post {
            url { host = ip }
            contentType(ContentType.Image.JPEG)
            setBody(imageFront.asAndroidBitmap().let {
                val stream = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.toByteArray()
            })
        }
    }
}