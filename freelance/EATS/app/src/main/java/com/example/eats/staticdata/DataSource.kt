package com.example.eats.staticdata

import com.example.eats.data.products.NutritionFacts
import com.example.eats.data.products.ProductInfo
import com.example.eats.data.userdata.Gender
import com.example.eats.data.userdata.User
import java.math.RoundingMode
import java.text.DecimalFormat

object DataSource {
    val productInfos = listOf(
        ProductInfo(
            id = "RotFront", label = "Конфеты Рот Фронт",
            nutrition100 = NutritionFacts(530f, 10f, 29f, 55f),
            null
        ),
        ProductInfo(
            "ChocoPie",
            "Чокопай",
            NutritionFacts(129f / 0.3f, 1.3f / 0.3f, 5.4f / 0.3f, 18.9f / 0.3f),
            null
        ),
        ProductInfo(
            "Ubileinoe",
            "Печенье Юбилейное",
            NutritionFacts(477f, 7.1f, 21f, 65f),
            null
        )
    )

    val recs = listOf(
        "Пейте больше воды",
        "Ешьте больше мяса",
        "Ешьте больше овощей"
    )
    val df = DecimalFormat("#.#").apply { roundingMode = RoundingMode.CEILING }
}

fun User.getCaloriesToEat(): Float = this.run {
    if (gender == Gender.Female)
        (10 * weight + 6.25f * height - 5 * age - 161) * activity.coefficient
    else
        (10 * weight + 6.25f * height - 5 * age + 5) * activity.coefficient
}.let { if (it < 0) 0f else it }