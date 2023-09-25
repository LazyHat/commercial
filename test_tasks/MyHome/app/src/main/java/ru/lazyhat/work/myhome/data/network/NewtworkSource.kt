package ru.lazyhat.work.myhome.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import ru.lazyhat.work.myhome.data.models.DTO.CamerasResponse
import ru.lazyhat.work.myhome.data.models.DTO.DoorsResponse
import ru.lazyhat.work.myhome.data.models.MainData
import ru.lazyhat.work.myhome.data.models.cameras.CameraInfo
import ru.lazyhat.work.myhome.data.models.cameras.CameraRoom
import ru.lazyhat.work.myhome.data.models.doors.DoorInfo

private const val camerasLink = "http://cars.cprogroup.ru/api/rubetek/cameras/"
private const val doorsLink = "http://cars.cprogroup.ru/api/rubetek/doors/"

interface NetworkSource {
    suspend fun getData(): MainDataResponse
}

class NetworkSourceInstance : NetworkSource {
    val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    override suspend fun getData(): MainDataResponse = try {
        MainDataResponse.Success(
            MainData(
                cameraRooms = client.get(camerasLink).body<CamerasResponse>().data.let {
                    (it.room + null).map { roomName ->
                        CameraRoom(
                            roomName,
                            it.cameras.filter { it.room == roomName }.map {
                                CameraInfo(
                                    it.id,
                                    it.name,
                                    it.snapshot,
                                    it.favorites,
                                    it.rec
                                )
                            }
                        )
                    }
                },
                doors = client.get(doorsLink).body<DoorsResponse>().data.map {
                    DoorInfo(
                        it.name,
                        it.room,
                        it.id,
                        it.favorites,
                        it.snapshot
                    )
                }
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
        MainDataResponse.Error
    }
}

sealed class MainDataResponse {
    data class Success(val data: MainData) : MainDataResponse()
    data object Error : MainDataResponse()
}