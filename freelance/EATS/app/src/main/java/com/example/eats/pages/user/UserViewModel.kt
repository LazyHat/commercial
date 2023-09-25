package com.example.eats.pages.user

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eats.data.userdata.Activeness
import com.example.eats.data.userdata.Gender
import com.example.eats.data.userdata.UserRepository
import com.example.eats.pages.eat.FieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val USER_BUNDLE_KEY = "user_key"

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val uiState: StateFlow<UserState> =
        savedStateHandle.getStateFlow(USER_BUNDLE_KEY, UserState().toBundle()).map {
            it.toUserState()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserState())

    init {
        viewModelScope.launch {
            savedStateHandle[USER_BUNDLE_KEY] = userRepository.getUserData().toState().toBundle()
        }
    }

    fun createEvent(e: UserEvent) = onEvent(e)

    private fun onEvent(e: UserEvent): Any = when (e) {
        is UserEvent.UpdateGender ->
            savedStateHandle[USER_BUNDLE_KEY] = uiState.value.copy(gender = e.new).toBundle()

        is UserEvent.UpdateActiveness ->
            savedStateHandle[USER_BUNDLE_KEY] = uiState.value.copy(activeness = e.new).toBundle()

        is UserEvent.UpdateAge -> {
            savedStateHandle[USER_BUNDLE_KEY] =
                uiState.value.copy(age = uiState.value.age.copy(value = e.new)).toBundle()
            if (uiState.value.age.error.isNotEmpty())
                savedStateHandle[USER_BUNDLE_KEY] =
                    uiState.value.copy(age = uiState.value.age.copy(error = "")).toBundle()
            else {
            }
        }

        is UserEvent.UpdateWeight -> {
            savedStateHandle[USER_BUNDLE_KEY] =
                uiState.value.copy(weight = uiState.value.weight.copy(value = e.new)).toBundle()
            if (uiState.value.weight.error.isNotEmpty())
                savedStateHandle[USER_BUNDLE_KEY] =
                    uiState.value.copy(weight = uiState.value.weight.copy(error = "")).toBundle()
            else {
            }
        }

        is UserEvent.UpdateHeight -> {
            savedStateHandle[USER_BUNDLE_KEY] =
                uiState.value.copy(height = uiState.value.height.copy(value = e.new)).toBundle()
            if (uiState.value.height.error.isNotEmpty())
                savedStateHandle[USER_BUNDLE_KEY] =
                    uiState.value.copy(height = uiState.value.height.copy(error = "")).toBundle()
            else {
            }
        }

        is UserEvent.Save ->
            @Suppress("ControlFlowWithEmptyBody")
            if (checkValues())
                viewModelScope.launch {
                    userRepository.updateUser(uiState.value.toUser())
                }
            else {
            }
    }

    private fun checkValues(): Boolean = true.run {
        if (uiState.value.height.value.isEmpty() || uiState.value.height.value.toFloat() >= 400) {
            savedStateHandle[USER_BUNDLE_KEY] =
                uiState.value.let {
                    it.copy(height = it.height.copy(error = "Неверное значение"))
                }.toBundle()
            false
        } else
            this
    }.run {
        if (uiState.value.weight.value.isEmpty() || uiState.value.weight.value.toFloat() >= 250) {
            savedStateHandle[USER_BUNDLE_KEY] =
                uiState.value.copy(weight = uiState.value.weight.copy(error = "Неверное значение"))
                    .toBundle()
            false
        } else
            this
    }.run {
        if (uiState.value.age.value.isEmpty() || uiState.value.age.value.toInt() >= 120) {
            savedStateHandle[USER_BUNDLE_KEY] =
                uiState.value.copy(age = uiState.value.age.copy(error = "Неверное значение"))
                    .toBundle()
            false
        } else
            this
    }
}

data class UserState(
    val height: FieldState = FieldState("", ""),
    val age: FieldState = FieldState("", ""),
    val weight: FieldState = FieldState("", ""),
    val activeness: Activeness = Activeness.Minimal,
    val gender: Gender = Gender.Male
)

sealed class UserEvent {
    data class UpdateWeight(val new: String) : UserEvent()
    data class UpdateAge(val new: String) : UserEvent()
    data class UpdateHeight(val new: String) : UserEvent()
    data class UpdateGender(val new: Gender) : UserEvent()
    data class UpdateActiveness(val new: Activeness) : UserEvent()
    object Save : UserEvent()
}