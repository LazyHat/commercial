package ru.lazyhat.work.nutriguide.data.products.db.day

import kotlinx.serialization.Serializable


data class Day(
    val time: DateTime,
    val caloriesDay: Float,
    val caloriesCurrent: Float,
    val eatBoxCalories: List<Float> = listOf(0f, 0f, 0f, 0f)
)

@Serializable
data class DateTime(
    val day: Int,
    val month: Int,
    val year: Int,
) : Comparable<DateTime> {
    override fun compareTo(other: DateTime): Int {
        return this.day - other.day + 31 * (this.month - other.month) + 365 * (this.year - other.year)
    }
}