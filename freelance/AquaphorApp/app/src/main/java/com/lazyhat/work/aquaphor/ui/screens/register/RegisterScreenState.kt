package com.lazyhat.work.aquaphor.ui.screens.register

//Модель данных для экрана регистрации
data class RegisterScreenState(
    val availableFilters: List<String> = listOf(),
    val currentFilter: String = "",
    val installDate: Long? = null,
    val error: String? = null
)