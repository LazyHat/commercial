package com.lazyhat.work.aquaphor.ui.screens.home.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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
import com.lazyhat.work.aquaphor.ui.theme.RalewayFamily
import java.time.LocalDate

//Страница с инструкцией
@Composable
fun InstructionPage(filter: Filter) {
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .fillMaxSize()
            .padding(30.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painterResource(id = R.drawable.header),
            "header",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(stringResource(id = R.string.instruction), style = BigRalewayStyle)
        Spacer(Modifier.height(20.dp))
        TextBox(
            label = if (filter.info.instruction.replaceRes != 0) stringResource(R.string.inst_install) else
                stringResource(id = R.string.inst_install_replace),
            stringRes = filter.info.instruction.installRes,
            imageRes = filter.info.instruction.imageRes
        )
        if (filter.info.instruction.replaceRes != 0) {
            Spacer(Modifier.height(20.dp))
            TextBox(
                label = stringResource(id = R.string.inst_replace),
                stringRes = filter.info.instruction.replaceRes
            )
        }
    }
}

@Composable
fun TextBox(
    label: String,
    stringRes: Int,
    imageRes: Int = 0
) {
    var visible by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(targetValue = if (visible) 180f else 0f)
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
        visible = !visible
    }) {
        Text(
            label,
            style = TextStyle(
                fontFamily = RalewayFamily,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Icon(
            painterResource(id = R.drawable.dropdown_string),
            "instruction",
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .size(20.dp)
                .rotate(rotation)
        )
    }
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(
            animationSpec = tween(500),
            expandFrom = Alignment.Top
        ) { 0 },
        exit = shrinkVertically(
            animationSpec = tween(500),
            shrinkTowards = Alignment.Top
        ) { 0 }
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (imageRes != 0) {
                Image(
                    painterResource(id = imageRes),
                    "install",
                    modifier = Modifier.fillMaxWidth(0.7f),
                    contentScale = ContentScale.FillWidth
                )
            }
            Text(
                stringResource(id = stringRes),
                style = TextStyle(fontFamily = RalewayFamily, fontSize = 22.sp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewInstructionPage() {
    AquaphorTheme {
        InstructionPage(
            filter = Filter(
                "Filter name",
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2023, 2, 2),
                info = Filters.availableInfos[0].second
            )
        )
    }
}