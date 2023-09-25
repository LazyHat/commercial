package ru.lazyhat.work.myhome.ui.screens.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.lazyhat.work.myhome.data.models.MainData
import ru.lazyhat.work.myhome.data.models.util.RefreshState
import ru.lazyhat.work.myhome.data.network.NetworkSource
import ru.lazyhat.work.myhome.data.repository.MainRepository
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    val uiState = combine(
        mainRepository.getDataFlow(),
        mainRepository.getRefreshDataFlow()
    ) { data, refreshState ->
        MainScreenState(
            data.cameraRooms,
            data.doors,
            refreshState
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MainScreenState(listOf(), listOf(), RefreshState(false))
    )

    init {
        viewModelScope.launch {
            mainRepository.refresh()
        }
    }

    fun createEvent(e: MainScreenEvent) = onEvent(e)

    private fun onEvent(e: MainScreenEvent): Any = when (e) {
        is MainScreenEvent.ChangeFavouriteCamera -> {
            viewModelScope.launch {
                mainRepository.updateCameraInfo(e.id) {
                    it.copy(favorite = !it.favorite)
                }
            }
        }

        is MainScreenEvent.ChangeFavouriteDoor -> {
            viewModelScope.launch {
                mainRepository.updateDoorInfo(e.id) {
                    it.copy(favorite = !it.favorite)
                }
            }
        }

        MainScreenEvent.Refresh -> {
            viewModelScope.launch {
                mainRepository.refresh()
            }
        }
    }
}