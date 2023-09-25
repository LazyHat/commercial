package com.lazyhat.work.aquaphor.domain.usecaseproviders

import com.lazyhat.work.aquaphor.domain.usecases.GetAvailableFiltersFlow
import com.lazyhat.work.aquaphor.domain.usecases.GetFilter
import com.lazyhat.work.aquaphor.domain.usecases.UpdateFilter

//Поставищик UseCase для экрана регистрации
data class RegisterUseCases(
    val getAvailableFiltersFlow: GetAvailableFiltersFlow,
    val updateFilter: UpdateFilter,
    val getFilter: GetFilter
)
