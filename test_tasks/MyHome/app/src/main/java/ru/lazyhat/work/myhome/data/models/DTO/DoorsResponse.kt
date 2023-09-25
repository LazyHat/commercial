package ru.lazyhat.work.myhome.data.models.DTO

import kotlinx.serialization.Serializable

@Serializable
data class DoorsResponse(
    val success: Boolean,
    val data: List<DoorInfoResponse>
)

@Serializable
data class DoorInfoResponse(
    val id: Int,
    val name: String,
    val snapshot: String? = null,
    val room: String?,
    val favorites: Boolean = false
)