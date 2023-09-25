package com.lazyhat.work.aquaphor.ui

import androidx.lifecycle.ViewModel
import com.lazyhat.work.aquaphor.domain.usecaseproviders.MainUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//Модель представления для Активити
@HiltViewModel
class MainActivityViewModel @Inject constructor(private val mainUseCases: MainUseCases) :
    ViewModel() {
    suspend fun dataIsNotEmpty() = mainUseCases.dataIsNotEmpty()
}