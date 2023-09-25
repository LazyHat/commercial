package com.lazyhat.work.aquaphor.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazyhat.work.aquaphor.data.models.Filter
import com.lazyhat.work.aquaphor.domain.usecaseproviders.HomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

//Модель представления
@HiltViewModel
class HomePagerViewModel @Inject constructor(private val homeUseCases: HomeUseCases) : ViewModel() {
    val uiState = homeUseCases.getFilterFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        Filter.Empty
    )
}