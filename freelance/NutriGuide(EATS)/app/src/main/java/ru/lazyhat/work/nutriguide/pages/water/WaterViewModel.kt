package ru.lazyhat.work.nutriguide.pages.water

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.lazyhat.work.nutriguide.data.userdata.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WaterState(val countGlasses: Int = 0, val caution: Boolean = false)

sealed class WaterEvent {
    object Plus : WaterEvent()
    object Minus : WaterEvent()
    object Reset : WaterEvent()
}

@HiltViewModel
class WaterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    val uiState: StateFlow<WaterState> =
        userRepository.getCountOfGlassesStream().map { WaterState(it, it > 16) }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000), WaterState()
        )

    fun createEvent(e: WaterEvent) = onEvent(e)

    @Suppress("IMPLICIT_CAST_TO_ANY")
    fun onEvent(e: WaterEvent) = when (e) {
        is WaterEvent.Plus -> {
            viewModelScope.launch {
                userRepository.updateCountOfGlasses(uiState.value.countGlasses + 1)
            }
        }

        is WaterEvent.Minus -> {
            @Suppress("ControlFlowWithEmptyBody")
            if (uiState.value.countGlasses > 0)
                viewModelScope.launch {
                    userRepository.updateCountOfGlasses(uiState.value.countGlasses - 1)
                } else {
            }
        }

        is WaterEvent.Reset -> {
            viewModelScope.launch {
                userRepository.updateCountOfGlasses(0)
            }
        }
    }
}