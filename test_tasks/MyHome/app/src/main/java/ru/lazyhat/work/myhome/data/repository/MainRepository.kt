package ru.lazyhat.work.myhome.data.repository

import kotlinx.coroutines.flow.Flow
import ru.lazyhat.work.myhome.data.models.MainData
import ru.lazyhat.work.myhome.data.models.cameras.CameraInfo
import ru.lazyhat.work.myhome.data.models.doors.DoorInfo
import ru.lazyhat.work.myhome.data.models.util.RefreshState

interface MainRepository {
    suspend fun updateDoorInfo(id: Int, update: (DoorInfo) -> DoorInfo)
    suspend fun refresh()
    fun getDataFlow(): Flow<MainData>
    suspend fun updateCameraInfo(id: Int, update: (CameraInfo) -> CameraInfo)

    fun getRefreshDataFlow(): Flow<RefreshState>
}