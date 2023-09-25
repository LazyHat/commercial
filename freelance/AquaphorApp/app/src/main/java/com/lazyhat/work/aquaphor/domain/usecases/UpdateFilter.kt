package com.lazyhat.work.aquaphor.domain.usecases

import com.lazyhat.work.aquaphor.data.repository.FilterRepository
import java.time.LocalDateTime
import java.time.ZoneOffset

//Запрос на обновление информации о фильтре
class UpdateFilter(private val filterRepository: FilterRepository) {
    suspend operator fun invoke(name: String, installDate: Long?): String? {
        return when {
            installDate == null -> "Дата не заполнена"
            !filterRepository.updateFilter(
                name,
                LocalDateTime.ofEpochSecond(
                    installDate / 1000,
                    0,
                    ZoneOffset.ofHours(3)
                ).toLocalDate().toEpochDay()
            ) -> "Введите фильтр"

            else -> null
        }
    }
}