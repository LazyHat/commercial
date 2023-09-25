package com.example.eats.pages.eat

import androidx.annotation.StringRes
import com.example.eats.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class EatTime(@StringRes val label: Int) {
    BreakFast(R.string.breakfast),
    Lunch(R.string.lunch),
    Dinner(R.string.dinner),
    Snack(R.string.snack)
}

data class EatScreenState(val time: EatTime = EatTime.BreakFast)

sealed class EatScreenEvent {
    data class UpdateTime(val new: EatTime) : EatScreenEvent()
}

class EatScreenViewModel(startTime: EatTime) {
    private val _uiState = MutableStateFlow(EatScreenState(startTime))
    val uiState: StateFlow<EatScreenState> = _uiState.asStateFlow()

    fun createEvent(e: EatScreenEvent) = onEvent(e)

    private fun onEvent(e: EatScreenEvent) = when (e) {
        is EatScreenEvent.UpdateTime -> _uiState.update {
            it.copy(time = e.new)
        }
    }
}