package com.lazyhat.work.aquaphor.domain.usecases

import com.lazyhat.work.aquaphor.data.repository.FilterRepository
import java.time.LocalDate

//Запрос даты замены фильтра
class GetExpiresDate(private val filterRepository: FilterRepository) {
    suspend operator fun invoke(): LocalDate {
        return filterRepository.getFilter()?.expiresDate ?: LocalDate.MIN
    }
}