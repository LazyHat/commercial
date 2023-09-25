package ru.lazyhat.work.activitytracker.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val name: String,
    val phone: String
)
