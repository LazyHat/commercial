package ru.lazyhat.work.nutriguide.pages.eat.pages.addinfo

import android.os.Bundle
import ru.lazyhat.work.nutriguide.pages.eat.getFieldState
import ru.lazyhat.work.nutriguide.pages.eat.putFieldState
import ru.lazyhat.work.nutriguide.data.products.NutritionFacts
import ru.lazyhat.work.nutriguide.data.products.ProductInfo
import kotlin.random.Random

const val LABEL_KEY = "label_b"
const val CALORIES_KEY = "cal_b"
const val PROTEINS_KEY = "prot_b"
const val FATS_KEY = "fats_b"
const val CARBOHYDRATES_KEY = "card"

fun AddInfoState.toBundle(): Bundle = Bundle().apply {
    putFieldState(LABEL_KEY, this@toBundle.label)
    putFieldState(CALORIES_KEY, this@toBundle.calories)
    putFieldState(PROTEINS_KEY, this@toBundle.proteins)
    putFieldState(FATS_KEY, this@toBundle.fats)
    putFieldState(CARBOHYDRATES_KEY, this@toBundle.carbohydrates)
}

fun Bundle.toAddInfoState(): AddInfoState = this.let {
    AddInfoState(
        it.getFieldState(LABEL_KEY),
        it.getFieldState(CALORIES_KEY),
        it.getFieldState(PROTEINS_KEY),
        it.getFieldState(FATS_KEY),
        it.getFieldState(CARBOHYDRATES_KEY)
    )
}

fun AddInfoState.toProductInfo(): ProductInfo = ProductInfo(
    id = Random.nextInt().toString(),
    label = label.value,
    nutrition100 = NutritionFacts(
        calories = calories.value.toFloat(),
        proteins = proteins.value.toFloat(),
        fats = fats.value.toFloat(),
        carbohydrates = carbohydrates.value.toFloat()
    ),
    null
)