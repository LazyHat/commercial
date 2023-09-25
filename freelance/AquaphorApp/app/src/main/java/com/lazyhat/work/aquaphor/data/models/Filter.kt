package com.lazyhat.work.aquaphor.data.models

import java.time.LocalDate
//Класс для хранения всей информации о фильтре
data class Filter(
    val name: String,
    val installDate: LocalDate,
    val expiresDate: LocalDate,
    val info: FilterInfo
) {
    companion object {
        val Empty = Filter("", LocalDate.MIN, LocalDate.MIN, FilterInfo.Empty)
    }
}
