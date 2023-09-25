package com.lazyhat.work.tracker.data.repo

import androidx.datastore.core.DataStore
import androidx.lifecycle.asFlow
import androidx.work.WorkManager
import com.lazyhat.work.tracker.data.model.AppData
import com.lazyhat.work.tracker.data.model.IpAddress
import com.lazyhat.work.tracker.data.work.MainWorker
import com.lazyhat.work.tracker.data.work.MainWorker.Companion.enqueueMainWorkerPeriodic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit

class MainRepositoryImpl(
    private val appDataStore: DataStore<AppData>,
    private val workManager: WorkManager
) : MainRepository {

    init {
        workManager.enqueueMainWorkerPeriodic()
    }

    override fun appDataFlow(): Flow<AppData> = appDataStore.data

    override suspend fun appData(): AppData = appDataStore.data.first()

    override fun isWorkerRunFlow(): Flow<Boolean> =
        workManager.getWorkInfosForUniqueWorkLiveData(MainWorker.WORKER_ID).asFlow()
            .map { workInfos ->
                workInfos.all { !it.state.isFinished }
            }

    override fun runWorker() {
        workManager.enqueueMainWorkerPeriodic()
    }

    override fun stopWorker() {
        workManager.cancelAllWork()
    }

    override suspend fun updateDuration(time: Long, unit: TimeUnit) {
        appDataStore.updateData { it.copy(duration = time, durationUnit = unit) }
    }

    override suspend fun updateIp(new: IpAddress) {
        appDataStore.updateData { it.copy(ip = new) }
    }
}