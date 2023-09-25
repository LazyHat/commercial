package com.lazyhat.work.aquaphor.ui.screens.home.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazyhat.work.aquaphor.R
import com.lazyhat.work.aquaphor.data.models.Filter
import com.lazyhat.work.aquaphor.data.static.Filters
import com.lazyhat.work.aquaphor.ui.theme.AquaphorTheme
import com.lazyhat.work.aquaphor.ui.theme.BigRalewayStyle
import com.lazyhat.work.aquaphor.ui.theme.MediumRalewayStyle
import com.lazyhat.work.aquaphor.ui.theme.RalewayFamily
import com.lazyhat.work.aquaphor.ui.theme.RobotoFamily
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

//Страница с основной информацией о фильтре
@Composable
fun FilterPage(filter: Filter, toRegister: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painterResource(id = R.drawable.header), "header")
            Image(
                painterResource(id = R.drawable.avatar),
                "avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clickable { toRegister() }
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(stringResource(id = R.string.filter_model), style = BigRalewayStyle)
            Text(
                filter.name,
                style = TextStyle(
                    fontFamily = RalewayFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 35.sp
                )
            )
        }
        AnimatedVisibility(visible = filter != Filter.Empty) {
            Image(
                painterResource(id = filter.info.imageRes),
                "image_of_filter",
                modifier = Modifier.fillMaxWidth(0.7f),
                contentScale = ContentScale.FillWidth
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                stringResource(id = R.string.date_of_install),
                style = MediumRalewayStyle
            )
            Text(
                filter.installDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
                style = TextStyle(fontFamily = RobotoFamily, fontSize = 30.sp)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                stringResource(id = R.string.date_of_replace),
                style = MediumRalewayStyle
            )
            Text(
                filter.expiresDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
                style = TextStyle(fontFamily = RobotoFamily, fontSize = 30.sp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewFilterPage() {
    AquaphorTheme {
        FilterPage(
            filter = Filter(
                "Filter name",
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2023, 2, 2),
                info = Filters.availableInfos[0].second
            )
        ) {}
    }
}