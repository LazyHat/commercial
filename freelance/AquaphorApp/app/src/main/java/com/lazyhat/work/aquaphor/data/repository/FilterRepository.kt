package com.lazyhat.work.aquaphor.data.repository

import com.lazyhat.work.aquaphor.data.models.Filter
import kotlinx.coroutines.flow.Flow

//Репозиторий для сообщения с хранилищем информации
interface FilterRepository {
    fun getAvailableFiltersFlow(): Flow<List<String>>
    suspend fun checkFilterExists(name: String): Boolean
    suspend fun updateFilter(name: String, installDateEpochDays: Long): Boolean
    fun getFilterFlow(): Flow<Filter>
    suspend fun getFilter(): Filter?
    suspend fun isDataExists(): Boolean
}