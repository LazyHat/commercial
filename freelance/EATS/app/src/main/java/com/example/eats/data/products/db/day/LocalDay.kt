package com.example.eats.data.products.db.day

import androidx.room.Entity
import androidx.room.PrimaryKey

const val DAY_TN = "days_db"

@Entity(tableName = DAY_TN)
data class LocalDay(
    @PrimaryKey
    val time: String,
    val caloriesDay: Float,
    val currentCalories: Float,
    val eatBoxCalories: String
)