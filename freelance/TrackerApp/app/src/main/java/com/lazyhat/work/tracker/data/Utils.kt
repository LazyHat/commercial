package com.lazyhat.work.tracker.data

import java.text.DecimalFormat

object Utils {
    val df = DecimalFormat("###.#####")
}

fun Double.format(df: DecimalFormat) = df.format(this)