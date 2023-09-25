package com.lazyhat.work.aquaphor.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lazyhat.work.aquaphor.R

//Экран загрузки
@Composable
fun LoadingScreen() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(30.dp)) {
        Image(
            painter = painterResource(id = R.drawable.header),
            "loading",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            contentScale = ContentScale.FillWidth
        )
    }
}