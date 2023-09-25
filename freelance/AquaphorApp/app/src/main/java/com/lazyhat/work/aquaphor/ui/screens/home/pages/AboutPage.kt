package com.lazyhat.work.aquaphor.ui.screens.home.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lazyhat.work.aquaphor.R
import com.lazyhat.work.aquaphor.ui.theme.AquaphorTheme
import com.lazyhat.work.aquaphor.ui.theme.BigRalewayStyle

//Страница о приложении
@Composable
fun AboutPage() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painter = painterResource(id = R.drawable.header),
            "header",
            Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Spacer(Modifier.height(30.dp))
        Text(
            stringResource(id = R.string.about),
            style = BigRalewayStyle
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewAboutPage() {
    AquaphorTheme {
        AboutPage()
    }
}