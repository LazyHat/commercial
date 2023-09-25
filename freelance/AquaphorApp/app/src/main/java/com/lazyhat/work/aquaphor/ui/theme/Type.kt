package com.lazyhat.work.aquaphor.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

//Стили текстов
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

val BigRalewayStyle = TextStyle(
    fontFamily = RalewayFamily,
    fontWeight = FontWeight.W800,
    fontSize = 40.sp
)

val MediumRalewayStyle = TextStyle(
    fontFamily = RalewayFamily,
    fontWeight = FontWeight.W800,
    fontSize = 30.sp
)