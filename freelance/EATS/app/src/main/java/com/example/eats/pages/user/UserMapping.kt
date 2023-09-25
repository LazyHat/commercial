package com.example.eats.pages.user

import android.os.Bundle
import com.example.eats.data.userdata.User
import com.example.eats.data.userdata.getActiveness
import com.example.eats.data.userdata.getGender
import com.example.eats.data.userdata.putActiveness
import com.example.eats.data.userdata.putGender
import com.example.eats.pages.eat.FieldState
import com.example.eats.pages.eat.getFieldState
import com.example.eats.pages.eat.putFieldState

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