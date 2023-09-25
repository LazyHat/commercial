package ru.lazyhat.work.activitytracker.data.sensors.base

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

abstract class MeasurableSensor(
    protected val sensorType: Int
) {
    protected var onSensorValuesChanged: ((List<Float>) -> Unit)? = null
    abstract val doesSensorExist: Boolean

    //abstract fun changeAccuracy()
    abstract fun startListening()
    abstract fun stopListening()
    fun getValuesFlow(): Flow<List<Float>> =
        callbackFlow {
            setOnSensorValuesChangedListener {
                trySend(it)
            }
            startListening()
            awaitClose { stopListening() }
        }

    fun setOnSensorValuesChangedListener(listener: (List<Float>) -> Unit) {
        onSensorValuesChanged = listener
    }
}