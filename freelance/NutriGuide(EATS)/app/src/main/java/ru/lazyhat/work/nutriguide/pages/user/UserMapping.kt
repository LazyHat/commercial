package ru.lazyhat.work.nutriguide.pages.user

import android.os.Bundle
import ru.lazyhat.work.nutriguide.data.userdata.User
import ru.lazyhat.work.nutriguide.data.userdata.getActiveness
import ru.lazyhat.work.nutriguide.data.userdata.getGender
import ru.lazyhat.work.nutriguide.data.userdata.putActiveness
import ru.lazyhat.work.nutriguide.data.userdata.putGender
import ru.lazyhat.work.nutriguide.pages.eat.FieldState
import ru.lazyhat.work.nutriguide.pages.eat.getFieldState
import ru.lazyhat.work.nutriguide.pages.eat.putFieldState

private const val HEIGHT_KEY = "height"
private const val WEIGHT_KEY = "weight"
private const val AGE_KEY = "age"
private const val GENDER_KEY = "gender"
private const val ACTIVENESS_KEY = "activeness"

fun Bundle.toUserState(): UserState = UserState(
    this.getFieldState(HEIGHT_KEY),
    this.getFieldState(AGE_KEY),
    this.getFieldState(WEIGHT_KEY),
    this.getActiveness(ACTIVENESS_KEY),
    this.getGender(GENDER_KEY)
)

fun User.toState(): UserState = UserState(
    FieldState(this.height.toString(), ""),
    FieldState(this.age.toString(), ""),
    FieldState(this.weight.toString(), ""),
    gender = gender,
    activeness = activity
)

fun UserState.toUser(): User = User(
    this.height.value.toFloat(),
    this.age.value.toInt(),
    this.weight.value.toFloat(),
    gender = gender,
    activity = activeness
)

fun UserState.toBundle(): Bundle = Bundle().apply {
    putFieldState(HEIGHT_KEY, height)
    putFieldState(AGE_KEY, age)
    putFieldState(WEIGHT_KEY, weight)
    putGender(GENDER_KEY, gender)
    putActiveness(ACTIVENESS_KEY, activeness)
}