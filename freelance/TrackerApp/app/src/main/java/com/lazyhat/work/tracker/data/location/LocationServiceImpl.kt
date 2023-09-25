package com.lazyhat.work.tracker.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.core.location.LocationListenerCompat
import com.lazyhat.work.tracker.Permissions

class LocationServiceImpl(
    private val context: Context
) : LocationService {

    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val locationListener = LocationListenerCompat {
        location = it
    }

    override var location: Location? = null
        private set
        get() {
            if (field == null)
                setUpListener()
            return field
        }

    @SuppressLint("MissingPermission")
    override fun setUpListener() {
        if (Permissions.checkGps(context))
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListener,
                Looper.getMainLooper()
            )
    }
}