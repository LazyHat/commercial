package com.lazyhat.work.tracker.data.location

import android.annotation.SuppressLint
import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationService {
    val location: Location?
    @SuppressLint("MissingPermission")
    fun setUpListener()
}