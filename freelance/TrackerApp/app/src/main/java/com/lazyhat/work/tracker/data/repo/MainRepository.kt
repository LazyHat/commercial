package com.lazyhat.work.tracker.data.repo

import com.lazyhat.work.tracker.data.model.AppData
import com.lazyhat.work.tracker.data.model.IpAddress
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

interface MainRepository {
    fun appDataFlow(): Flow<AppData>
    suspend fun appData(): AppData
    suspend fun updateIp(new: IpAddress)
    suspend fun updateDuration(time: Long, unit: TimeUnit)
    fun isWorkerRunFlow(): Flow<Boolean>
    fun runWorker()
    fun stopWorker()
}