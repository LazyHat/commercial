package ru.lazyhat.work.nutriguide.data.products

data class ProductState(
    val weight: Float,
    val info: ProductInfo
)

data class ProductInfo(
    val id: String,
    val label: String,
    val nutrition100: NutritionFacts,
    val pieceWeight: Float?
)

data class NutritionFacts(
    val calories: Float,
    val proteins: Float,
    val fats: Float,
    val carbohydrates: Float,
)