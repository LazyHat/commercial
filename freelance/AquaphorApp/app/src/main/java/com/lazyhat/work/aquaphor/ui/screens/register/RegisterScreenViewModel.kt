package com.lazyhat.work.aquaphor.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazyhat.work.aquaphor.data.models.FilterData
import com.lazyhat.work.aquaphor.domain.usecaseproviders.RegisterUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

//Модель представления для экрана регистрации
@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    private val registerUseCases: RegisterUseCases
) : ViewModel() {
    private val _currentFilter = MutableStateFlow("")
    private val _currentDate = MutableStateFlow<Long?>(null)
    private val _error = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            val data = registerUseCases.getFilter()
            if (data != null) {
                _currentFilter.update { data.name }
                _currentDate.update { data.installDate.toEpochDay() * 24 * 3600 * 1000 }
            }
        }
    }

    val uiState = combine(
        registerUseCases.getAvailableFiltersFlow(),
        _currentFilter,
        _currentDate,
        _error
    ) { filters, cFilter, cDate, dError ->
        RegisterScreenState(
            filters,
            cFilter,
            cDate,
            dError
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RegisterScreenState())

    fun createEvent(e: RegisterScreenEvent) = onEvent(e)

    private fun onEvent(e: RegisterScreenEvent): Any = when (e) {
        is RegisterScreenEvent.DateChange -> {
            _currentDate.update { e.new }
            _error.update { null }
        }

        is RegisterScreenEvent.FilterChange -> {
            _currentFilter.update { e.new }
            _error.update { null }
        }

        is RegisterScreenEvent.Save -> {
            viewModelScope.launch {
                if (_error.value != null)
                    return@launch
                val result = registerUseCases.updateFilter(_currentFilter.value, _currentDate.value)
                if (result.isNullOrEmpty())
                    e.onResult.invoke()
                else
                    _error.update { result }
            }
        }
    }
}