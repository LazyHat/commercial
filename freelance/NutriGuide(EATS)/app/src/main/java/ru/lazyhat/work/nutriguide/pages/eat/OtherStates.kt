package ru.lazyhat.work.nutriguide.pages.eat

import android.os.Bundle

data class FieldState(val value: String, val error: String) {
    companion object {
        fun empty() = FieldState("", "")
    }
}

fun Bundle.putFieldState(key: String, value: FieldState) {
    this.putString(key, value.value)
    this.putString("${key}_E", value.error)
}

fun Bundle.getFieldState(key: String): FieldState =
    FieldState(
        this.getString(key, ""),
        this.getString("${key}_E", "")
    )
