package com.lazyhat.work.tracker.data.network

import kotlinx.serialization.Serializable

@Serializable
data class SendData(val battery: Int, val lat: Double, val long: Double)
