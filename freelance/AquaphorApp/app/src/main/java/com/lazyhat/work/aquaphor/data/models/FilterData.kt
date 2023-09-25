package com.lazyhat.work.aquaphor.data.models

import kotlinx.serialization.Serializable

//Класс для хранения данных о фильтре в хранилище
@Serializable
data class FilterData(
    val name: String,
    val installDateEpochDays: Long,
    val expiresDateEpochDays: Long
) {
    fun isEmpty(): Boolean = this == Empty

    companion object {
        val Empty = FilterData("", 0, 0)
    }
}
