package com.lazyhat.work.aquaphor.ui.screens.home

import com.lazyhat.work.aquaphor.R

//Собственно страницы пейджера
enum class HomePages(val label: String, val tabImageRes: Int) {
    Instruction("Инструкция", R.drawable.ic_instruction),
    Filter("МойФильтр", R.drawable.ic_filter),
    About("О приложении", R.drawable.ic_info)
}