package com.lazyhat.work.aquaphor.domain.usecaseproviders

import com.lazyhat.work.aquaphor.domain.usecases.GetExpiresDate

//Поставищик UseCase для воркера
data class DateWorkerUseCases(
    val getExpiresDate: GetExpiresDate
)