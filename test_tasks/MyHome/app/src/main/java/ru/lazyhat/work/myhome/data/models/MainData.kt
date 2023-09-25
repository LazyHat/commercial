package ru.lazyhat.work.myhome.data.models

import ru.lazyhat.work.myhome.data.models.cameras.CameraRoom
import ru.lazyhat.work.myhome.data.models.doors.DoorInfo

data class MainData(
    val cameraRooms: List<CameraRoom>,
    val doors: List<DoorInfo>
)