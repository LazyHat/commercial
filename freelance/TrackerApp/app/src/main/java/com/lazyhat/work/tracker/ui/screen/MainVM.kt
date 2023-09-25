package com.lazyhat.work.tracker.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazyhat.work.tracker.data.model.IpAddress
import com.lazyhat.work.tracker.data.repo.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    private val _ip = MutableStateFlow("")
    private val _error = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            _ip.value = mainRepository.appData().ip.toString()
        }
    }

    val uiState = combine(
        mainRepository.appDataFlow(),
        mainRepository.isWorkerRunFlow(),
        _ip.asStateFlow(),
        _error.asStateFlow()
    ) { appData, status, cIp, err ->
        MainState(
            status,
            appData.batteryLevel,
            appData.lat,
            appData.long,
            appData.ip.toString(),
            cIp,
            appData.duration,
            appData.durationUnit,
            appData.pictureFront?.toImageBitmap(),
            appData.pictureBack?.toImageBitmap(),
            appData.realUpdateInterval,
            err
        )
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), MainState()
    )

    fun createEvent(e: MainEvent) = onEvent(e)

    private fun onEvent(e: MainEvent): Any = when (e) {
        is MainEvent.UpdateIp -> {
            _ip.value = e.new
            _error.value = null
        }

        is MainEvent.SaveIp -> viewModelScope.launch {
            try {
                mainRepository.updateIp(IpAddress.Parse(_ip.value))
            } catch (e: IllegalArgumentException) {
                _error.value = e.message
            }
        }

        is MainEvent.RunSender -> mainRepository.runWorker()
        is MainEvent.StopSender -> mainRepository.stopWorker()

        is MainEvent.UpdateDuration -> viewModelScope.launch {
            mainRepository.updateDuration(e.time, e.unit)
        }
    }
}