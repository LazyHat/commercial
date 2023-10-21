package ru.lazyhat.work.nutriguide.pages.recs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.lazyhat.work.nutriguide.pages.utils.TopBar
import ru.lazyhat.work.nutriguide.staticdata.DataSource
import ru.lazyhat.work.nutriguide.theme.NutriGuideTheme
import ru.lazyhat.work.nutriguide.R

@Composable
fun RecsPage() {
    Scaffold(topBar = { TopBar(label = stringResource(id = R.string.page_recs_label)) }) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = DataSource.recs.random(),
                modifier = Modifier.padding(10.dp),
                style = TextStyle(fontSize = 20.sp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewRecsPage() {
    NutriGuideTheme {
        RecsPage()
    }
}