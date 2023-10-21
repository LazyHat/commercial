package ru.lazyhat.work.nutriguide.pages.water

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.lazyhat.work.nutriguide.R
import ru.lazyhat.work.nutriguide.pages.utils.TopBar
import ru.lazyhat.work.nutriguide.theme.NutriGuideTheme

@Composable
fun WaterPage(viewModel: WaterViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val onEvent: (e: WaterEvent) -> Unit = { viewModel.createEvent(it) }
    var orientation by remember { mutableStateOf(Configuration.ORIENTATION_PORTRAIT) }
    val configuration = LocalConfiguration.current
    LaunchedEffect(key1 = configuration) {
        snapshotFlow { configuration.orientation }.collect { orientation = it }
    }

    Scaffold(topBar = { TopBar(label = stringResource(id = R.string.page_water_label)) }) {
        Box(Modifier.padding(it)) {
            when (orientation) {
                Configuration.ORIENTATION_LANDSCAPE ->
                    LandScapeLayout(uiState = uiState, onEvent = onEvent)

                else ->
                    PortraitLayout(uiState = uiState, onEvent = onEvent)
            }
        }
    }
}

@Composable
private fun LandScapeLayout(uiState: WaterState, onEvent: (e: WaterEvent) -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Title(caution = uiState.caution)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            CountGlasses(count = uiState.countGlasses)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MinusButton(onEvent = onEvent)
                    PlusButton(onEvent = onEvent)
                }
                ResetButton(onEvent = onEvent)
            }
        }
    }
}

@Composable
private fun PortraitLayout(uiState: WaterState, onEvent: (e: WaterEvent) -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Title(caution = uiState.caution)
        CountGlasses(count = uiState.countGlasses)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                MinusButton(onEvent = onEvent)
                PlusButton(onEvent = onEvent)
            }
            ResetButton(onEvent = onEvent)
        }
    }
}

@Composable
private fun Title(caution: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Количество выпитых стаканов воды", style = TextStyle(fontSize = 20.sp))
        Text(
            text = if (caution) "Вы випили слишком много воды!" else "",
            style = TextStyle(fontSize = 15.sp),
            color = Color.Red
        )
    }
}

@Composable
private fun CountGlasses(count: Int) {
    Text(text = count.toString(), style = TextStyle(fontSize = 70.sp))
}

@Composable
private fun PlusButton(onEvent: (e: WaterEvent) -> Unit) {
    Button(
        onClick = { onEvent(WaterEvent.Plus) },
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text = "+", style = TextStyle(fontSize = 20.sp))
    }
}

@Composable
private fun MinusButton(onEvent: (e: WaterEvent) -> Unit) {
    Button(
        onClick = { onEvent(WaterEvent.Minus) },
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text = "-", style = TextStyle(fontSize = 20.sp))
    }
}

@Composable
private fun ResetButton(onEvent: (e: WaterEvent) -> Unit) {
    Button(
        onClick = { onEvent(WaterEvent.Reset) },
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text = "СТЕРЕТЬ", style = TextStyle(fontSize = 15.sp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPortrait() {
    NutriGuideTheme {
        PortraitLayout(uiState = WaterState(20, true), onEvent = {})
    }
}

@Preview(showBackground = true, device = "spec:shape=Normal,width=720,height=360,unit=dp,dpi=420")
@Composable
fun PreviewLandScape() {
    NutriGuideTheme {
        LandScapeLayout(uiState = WaterState(20, true), onEvent = {})
    }
}