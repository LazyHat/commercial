package ru.lazyhat.work.myhome.data.repository

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.lazyhat.work.myhome.data.db.tables.CameraObject
import ru.lazyhat.work.myhome.data.db.tables.DoorObject
import ru.lazyhat.work.myhome.data.db.tables.toCameraInfo
import ru.lazyhat.work.myhome.data.db.tables.toCameraObject
import ru.lazyhat.work.myhome.data.db.tables.toCameraObjects
import ru.lazyhat.work.myhome.data.db.tables.toCameraRooms
import ru.lazyhat.work.myhome.data.db.tables.toDoorInfo
import ru.lazyhat.work.myhome.data.db.tables.toDoorInfos
import ru.lazyhat.work.myhome.data.db.tables.toDoorObject
import ru.lazyhat.work.myhome.data.db.tables.toDoorObjects
import ru.lazyhat.work.myhome.data.models.MainData
import ru.lazyhat.work.myhome.data.models.cameras.CameraInfo
import ru.lazyhat.work.myhome.data.models.doors.DoorInfo
import ru.lazyhat.work.myhome.data.models.util.RefreshState
import ru.lazyhat.work.myhome.data.network.MainDataResponse
import ru.lazyhat.work.myhome.data.network.NetworkSource

class MainRepositoryImpl(private val realm: Realm, private val networkSource: NetworkSource) :
    MainRepository {

    val scope = CoroutineScope(Dispatchers.IO)
    val refreshState = MutableStateFlow(RefreshState(false))


    init {
        scope.launch {
            refresh()
        }
    }

    override fun getDataFlow(): Flow<MainData> =
        combine(
            realm.query<CameraObject>().asFlow(),
            realm.query<DoorObject>().asFlow()
        ) { cameras, doors ->
            MainData(
                cameras.list.toCameraRooms().sortedBy { it.name }
                    .map { it.copy(cameras = it.cameras.sortedBy { it.name }) },
                doors.list.toDoorInfos().sortedBy { it.name }
            )
        }

    override suspend fun updateCameraInfo(id: Int, update: (CameraInfo) -> CameraInfo) =
        withContext(Dispatchers.IO) {
            realm.writeBlocking {
                val prev = realm.query<CameraObject>("id == $id").first().find()
                require(prev != null) { "MainRepositoryImpl: updateDoorInfo() no cameraObjects matches current id" }
                val room = prev.room
                val new = update(prev.toCameraInfo())
                findLatest(prev)?.also {
                    delete(it)
                }
                copyToRealm(new.toCameraObject(room))
            }
            Unit
        }

    override fun getRefreshDataFlow(): Flow<RefreshState> = refreshState

    override suspend fun updateDoorInfo(id: Int, update: (DoorInfo) -> DoorInfo) =
        withContext(Dispatchers.IO) {
            realm.writeBlocking {
                val prev = realm.query<DoorObject>("id == $id").first().find()
                require(prev != null) { "MainRepositoryImpl: updateDoorInfo() no doorObjects matches current id" }
                findLatest(prev)?.also {
                    delete(it)
                }
                copyToRealm(update(prev.toDoorInfo()).toDoorObject())
            }
            Unit
        }

    override suspend fun refresh() {
        refreshState.update { it.copy(isLoading = true) }
        val cameras = realm.query<CameraObject>().find()
        val doors = realm.query<DoorObject>().find()
        networkSource.getData().let {
            if (it is MainDataResponse.Success) {
                realm.writeBlocking {
                    deleteAll()
                    it.data.cameraRooms.toCameraObjects().forEach {
                        copyToRealm(it)
                    }
                    it.data.doors.toDoorObjects().forEach {
                        copyToRealm(it)
                    }
                }
            }
        }
        refreshState.update { it.copy(isLoading = false) }
    }
}

