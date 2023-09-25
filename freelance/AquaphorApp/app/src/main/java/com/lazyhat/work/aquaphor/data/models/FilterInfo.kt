package com.lazyhat.work.aquaphor.data.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

//Класс для хранения статичной информации о фильтрах
data class FilterInfo(
    @DrawableRes val imageRes: Int,
    val instruction: Instruction
) {
    companion object {
        val Empty = FilterInfo(0, Instruction.Empty)
    }
}

data class Instruction(
    @DrawableRes val imageRes: Int,
    @StringRes val installRes: Int,
    @StringRes val replaceRes: Int
) {
    companion object {
        val Empty = Instruction(0, 0, 0)
    }
}
