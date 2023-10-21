package ru.lazyhat.work.nutriguide.data.products.db.infos

import androidx.room.Entity
import androidx.room.PrimaryKey

const val INFO_TN = "local_info"

@Entity(tableName = INFO_TN)
data class LocalInfo(
    @PrimaryKey
    val id: String,
    val label: String,
    val calories100: Float,
    val proteins100: Float,
    val fats100: Float,
    val carbohydrates100: Float,
    val pieceWeight: Float?
)