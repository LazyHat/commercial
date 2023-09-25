package com.lazyhat.work.aquaphor.domain.usecases

import com.lazyhat.work.aquaphor.data.models.Filter
import com.lazyhat.work.aquaphor.data.repository.FilterRepository

//Запрос информации о фильтре
class GetFilter(private val filterRepository: FilterRepository) {
    suspend operator fun invoke(): Filter? {
        return filterRepository.getFilter()
    }
}