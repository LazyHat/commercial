package com.lazyhat.work.aquaphor.domain.usecases

import com.lazyhat.work.aquaphor.data.repository.FilterRepository

//Запрос на проверку хранилища на предмет отсутствия данных
class DataIsNotEmpty(private val filterRepository: FilterRepository) {
    suspend operator fun invoke(): Boolean {
        return filterRepository.isDataExists()
    }
}