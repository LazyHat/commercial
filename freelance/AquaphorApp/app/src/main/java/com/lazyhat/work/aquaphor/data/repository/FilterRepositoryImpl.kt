package com.lazyhat.work.aquaphor.data.repository

import androidx.datastore.core.DataStore
import com.lazyhat.work.aquaphor.data.models.Filter
import com.lazyhat.work.aquaphor.data.static.Filters
import com.lazyhat.work.aquaphor.data.models.FilterData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import java.time.LocalDate

//Реализация репозитория
class FilterRepositoryImpl(private val dataStore: DataStore<FilterData>) : FilterRepository {
    override fun getAvailableFiltersFlow(): Flow<List<String>> {
        return MutableStateFlow(Filters.availableInfos.map { it.first }).asStateFlow()
    }

    override suspend fun checkFilterExists(name: String): Boolean {
        return Filters.availableInfos.find { it.first == name } != null
    }

    override suspend fun updateFilter(name: String, installDateEpochDays: Long): Boolean {
        if (!checkFilterExists(name)) return false
        dataStore.updateData {
            FilterData(
                name,
                installDateEpochDays,
                LocalDate.ofEpochDay(installDateEpochDays).plusYears(1).toEpochDay()
            )
        }
        return true
    }

    override fun getFilterFlow(): Flow<Filter> {
        return dataStore.data.mapNotNull { data ->
            val filter = Filters.availableInfos.find { it.first == data.name }
            if (filter == null) null
            else Filter(
                data.name,
                LocalDate.ofEpochDay(data.installDateEpochDays),
                LocalDate.ofEpochDay(data.expiresDateEpochDays),
                filter.second
            )
        }
    }

    override suspend fun getFilter(): Filter? {
        val filterData = dataStore.data.first()
        return if (checkFilterExists(filterData.name))
            Filter(
                name = filterData.name,
                installDate = LocalDate.ofEpochDay(filterData.installDateEpochDays),
                expiresDate = LocalDate.ofEpochDay(filterData.expiresDateEpochDays),
                info = Filters.availableInfos.find { it.first == filterData.name }!!.second
            )
        else
            null
    }

    override suspend fun isDataExists(): Boolean {
        return dataStore.data.first() != FilterData.Empty
    }
}
