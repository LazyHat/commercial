package com.lazyhat.work.aquaphor.domain.usecases

import com.lazyhat.work.aquaphor.data.repository.FilterRepository
import kotlinx.coroutines.flow.Flow

//Запрос существующих фильтров
class GetAvailableFiltersFlow(private val filterRepository: FilterRepository) {
    operator fun invoke(): Flow<List<String>> = filterRepository.getAvailableFiltersFlow()
}