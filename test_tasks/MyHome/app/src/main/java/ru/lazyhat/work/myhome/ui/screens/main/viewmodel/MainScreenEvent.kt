package ru.lazyhat.work.myhome.ui.screens.main.viewmodel

sealed class MainScreenEvent {
    data class ChangeFavouriteCamera(val id: Int) : MainScreenEvent()
    data class ChangeFavouriteDoor(val id: Int) : MainScreenEvent()
    data object Refresh : MainScreenEvent()
}