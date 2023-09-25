package com.lazyhat.work.aquaphor.ui.screens.register

//Обратная связь UI
sealed class RegisterScreenEvent {
    data class FilterChange(val new: String) : RegisterScreenEvent()
    data class DateChange(val new: Long?) : RegisterScreenEvent()
    data class Save(val onResult: () -> Unit) : RegisterScreenEvent()
}
