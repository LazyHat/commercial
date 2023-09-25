package com.lazyhat.work.aquaphor.domain.usecaseproviders

import com.lazyhat.work.aquaphor.domain.usecases.DataIsNotEmpty

//Поставищик UseCase для Activity
data class MainUseCases(
    val dataIsNotEmpty: DataIsNotEmpty
)