package ru.lazyhat.work.nutriguide.data.products.db.products

import androidx.room.Entity

const val PRODUCT_TN = "products"

@Entity(tableName = PRODUCT_TN, primaryKeys = ["time", "id"])
data class LocalProduct(
    val time: String,
    val id: String,
    val weight: Float
)