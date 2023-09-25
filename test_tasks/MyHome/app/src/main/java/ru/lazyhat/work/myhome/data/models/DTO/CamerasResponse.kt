package ru.lazyhat.work.myhome.data.models.DTO

import kotlinx.serialization.Serializable

@Serializable
data class CamerasResponse(
    val success: Boolean,
    val data: CameraData
)

@Serializable
data class CameraData(
    val room: List<String>,
    val cameras: List<CameraInfoResponse>
)

@Serializable
data class CameraInfoResponse(
    val name: String,
    val snapshot: String,
    val room: String?,
    val id: Int,
    val favorites: Boolean,
    val rec: Boolean
)