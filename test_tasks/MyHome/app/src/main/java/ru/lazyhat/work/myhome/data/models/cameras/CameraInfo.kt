package ru.lazyhat.work.myhome.data.models.cameras

import kotlinx.serialization.Serializable

@Serializable
data class CameraInfo(
    val id: Int,
    val name: String,
    val snapshot: String,
    val favorite: Boolean,
    val rec: Boolean
)