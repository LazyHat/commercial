package ru.lazyhat.work.nutriguide.data.userdata

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import ru.lazyhat.work.nutriguide.theme.Green
import ru.lazyhat.work.nutriguide.R

data class User(
    val height: Float = 0f,
    val age: Int = 0,
    val weight: Float = 0f,
    val gender: Gender = Gender.Female,
    val activity: Activeness = Activeness.Minimal,
)

enum class Gender { Male, Female; }

fun Bundle.putGender(key: String, gender: Gender) =
    this.putString(key, gender.name)

fun Bundle.getGender(
    key: String,
    defaultValue: Gender = Gender.Male
): Gender =
    Gender.valueOf(this.getString(key, defaultValue.name))

enum class Activeness(
    val coefficient: Float,
    val label: String,
    @StringRes val explanation: Int,
    val color: Color
) {
    Minimal(1.2f, "Минимальная активность", R.string.minimal_activeness_explain, Color(0xFF0099ff)),
    Low(1.375f, "Слабая активность", R.string.low_activeness_explain, Green),
    Medium(1.55f, "Средняя активность", R.string.medium_activeness_explain, Color(0xFFcccc00)),
    Hard(1.7f, "Сильная активность", R.string.hard_activeness_explain, Color(0xFFff8c1a)),
    Extreme(
        1.9f,
        "Екстремальная активность",
        R.string.extreme_activeness_explain,
        Color(0xFFff3300)
    );
}

fun Bundle.putActiveness(key: String, activeness: Activeness) =
    this.putString(key, activeness.name)

fun Bundle.getActiveness(key: String, defaultValue: Activeness = Activeness.Minimal): Activeness =
    Activeness.valueOf(this.getString(key, defaultValue.name))
