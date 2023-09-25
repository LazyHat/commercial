package com.example.eats.data.userdata

import kotlinx.serialization.Serializable

@Serializable
data class LocalUser(
    val height: Float = 0f,
    val age: Int = 0,
    val weight: Float = 0f,
    val gender: Gender = Gender.Female,
    val activeness: Activeness = Activeness.Minimal,
    val countGlasses: Int = 0
)