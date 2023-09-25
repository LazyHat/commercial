package ru.lazyhat.work.myhome.data.db.tables

import ru.lazyhat.work.myhome.data.models.cameras.CameraInfo
import ru.lazyhat.work.myhome.data.models.cameras.CameraRoom
import ru.lazyhat.work.myhome.data.models.doors.DoorInfo

fun List<DoorObject>.toDoorInfos(): List<DoorInfo> =
    map {
        DoorInfo(
            it.name,
            it.room,
            it.id,
            it.favorite,
            it.snapshot
        )
    }

fun List<CameraObject>.toCameraRooms(): List<CameraRoom> =
    map { it.room }.toSet().map { room ->
        CameraRoom(
            room,
            filter { it.room == room }.map {
                CameraInfo(
                    it.id,
                    it.name,
                    it.snapshot,
                    it.favorite,
                    it.rec
                )
            }
        )
    }

fun List<CameraRoom>.toCameraObjects(): List<CameraObject> =
    fold(listOf()) { acc: List<CameraObject>, cameraRoom: CameraRoom ->
        acc + cameraRoom.cameras.map {
            CameraObject().apply {
                id = it.id
                name = it.name
                snapshot = it.snapshot
                rec = it.rec
                favorite = it.favorite
                room = cameraRoom.name
            }
        }
    }

fun List<DoorInfo>.toDoorObjects(): List<DoorObject> =
    map {
        DoorObject().apply {
            id = it.id
            name = it.name
            room = it.room
            favorite = it.favorite
            snapshot = it.snapshot
        }
    }

fun CameraObject.toCameraInfo(): CameraInfo = CameraInfo(id, name, snapshot, favorite, rec)
fun CameraInfo.toCameraObject(room: String?): CameraObject = CameraObject().apply {
    id = this@toCameraObject.id
    name = this@toCameraObject.name
    this.room = room
    favorite = this@toCameraObject.favorite
    snapshot = this@toCameraObject.snapshot
    rec = this@toCameraObject.rec
}

fun DoorObject.toDoorInfo(): DoorInfo = DoorInfo(name, room, id, favorite, snapshot)
fun DoorInfo.toDoorObject(): DoorObject = DoorObject().apply {
    id = this@toDoorObject.id
    name = this@toDoorObject.name
    room = this@toDoorObject.room
    favorite = this@toDoorObject.favorite
    snapshot = this@toDoorObject.snapshot
}