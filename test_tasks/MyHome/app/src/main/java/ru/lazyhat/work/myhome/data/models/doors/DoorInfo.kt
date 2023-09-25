package ru.lazyhat.work.myhome.data.models.doors

import kotlinx.serialization.Serializable

@Serializable
data class DoorInfo(
    val name: String,
    val room: String?,
    val id: Int,
    val favorite: Boolean,
    val snapshot: String? = null
)
