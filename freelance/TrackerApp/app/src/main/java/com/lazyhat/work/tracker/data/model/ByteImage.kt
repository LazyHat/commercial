package com.lazyhat.work.tracker.data.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.serialization.Serializable
import java.io.ByteArrayOutputStream

@Serializable
data class ByteImage(
    val image: ByteArray
) {

    constructor(bitmap: ImageBitmap) : this(bitmap.let {
        val stream = ByteArrayOutputStream()
        bitmap.asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.toByteArray()
    })

    fun toImageBitmap(): ImageBitmap =
        BitmapFactory.decodeByteArray(image, 0, image.size).asImageBitmap()


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ByteImage

        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        return image.contentHashCode()
    }
}
