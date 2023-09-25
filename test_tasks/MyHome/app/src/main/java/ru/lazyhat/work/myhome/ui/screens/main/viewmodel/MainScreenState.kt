package ru.lazyhat.work.myhome.ui.screens.main.viewmodel

import ru.lazyhat.work.myhome.data.models.cameras.CameraRoom
import ru.lazyhat.work.myhome.data.models.doors.DoorInfo
import ru.lazyhat.work.myhome.data.models.util.RefreshState

data class MainScreenState(
    val cameraRooms: List<CameraRoom>,
    val doors: List<DoorInfo>,
    val refreshState: RefreshState
)



