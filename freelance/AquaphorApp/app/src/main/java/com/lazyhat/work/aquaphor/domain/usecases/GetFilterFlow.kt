package com.lazyhat.work.aquaphor.domain.usecases

import com.lazyhat.work.aquaphor.data.models.Filter
import com.lazyhat.work.aquaphor.data.repository.FilterRepository
import kotlinx.coroutines.flow.Flow

//Запрос информации о фильтре в потоке
class GetFilterFlow(private val filterRepository: FilterRepository) {
    operator fun invoke(): Flow<Filter> {
        return filterRepository.getFilterFlow()
    }
}