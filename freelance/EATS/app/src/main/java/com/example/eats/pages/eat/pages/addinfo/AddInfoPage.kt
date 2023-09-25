package com.example.eats.pages.eat.pages.addinfo

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eats.data.formatAsFloat
import com.example.eats.pages.eat.FieldState
import com.example.eats.pages.utils.EATSTextField
import com.example.eats.pages.utils.TopBar
import com.example.eats.ui.theme.EATSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInfoPage(viewModel: AddInfoViewModel = hiltViewModel(), onResult: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val onEvent: (e: AddInfoEvent) -> Unit = {
        viewModel.createEvent(it)
    }
    var orientation by remember { mutableStateOf(Configuration.ORIENTATION_PORTRAIT) }
    val configuration = LocalConfiguration.current
    val scrollState = rememberScrollState()
    LaunchedEffect(key1 = configuration) {
        snapshotFlow { configuration.orientation }.collect { orientation = it }
    }
    Scaffold(topBar = { TopBar(label = "Новый продукт") }) {
        Box(
            Modifier
                .padding(it)
                .verticalScroll(scrollState)) {
            when (orientation) {
                Configuration.ORIENTATION_LANDSCAPE ->
                    LandScapeLayout(uiState, onEvent, onResult)

                else ->
                    PortraitLayout(uiState, onEvent, onResult)
            }
        }
    }
}

@Composable
private fun PortraitLayout(
    uiState: AddInfoState,
    onEvent: (e: AddInfoEvent) -> Unit,
    onResult: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        LabelTextField(state = uiState.label, onEvent = onEvent)
        NutritionText()
        CaloriesTextField(state = uiState.calories, onEvent = onEvent)
        ProteinsTextField(state = uiState.proteins, onEvent = onEvent)
        FatsTextField(state = uiState.fats, onEvent = onEvent)
        CarbohydratesTextField(state = uiState.carbohydrates, onEvent = onEvent)
        ResultButton(onClick = { onEvent(AddInfoEvent.Result(onResult)) })
    }
}

@Composable
private fun LandScapeLayout(
    uiState: AddInfoState,
    onEvent: (e: AddInfoEvent) -> Unit,
    onResult: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .padding(top = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LabelTextField(state = uiState.label, onEvent = onEvent)
        NutritionText()
        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
            Column() {
                CaloriesTextField(state = uiState.calories, onEvent = onEvent)
                ProteinsTextField(state = uiState.proteins, onEvent = onEvent)
            }
            Column() {
                FatsTextField(state = uiState.fats, onEvent = onEvent)
                CarbohydratesTextField(state = uiState.carbohydrates, onEvent = onEvent)
            }
        }
        ResultButton(onClick = { onEvent(AddInfoEvent.Result(onResult)) })
    }
}

@Composable
private fun LabelTextField(state: FieldState, onEvent: (e: AddInfoEvent) -> Unit) {
    EATSTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.value,
        error = state.error,
        label = { Text("Название:", style = TextStyle(fontSize = 14.sp)) },
        hintText = "Введите название",
        style = TextStyle(fontSize = 18.sp),
        onValueChange = { onEvent(AddInfoEvent.LabelChange(it)) })
}

@Composable
private fun NutritionText() {
    Text("Пищевая ценность на 100 г.")
}

@Composable
private fun CaloriesTextField(state: FieldState, onEvent: (e: AddInfoEvent) -> Unit) {
    EATSTextField(
        value = state.value,
        error = state.error,
        label = { Text("Калории:", style = TextStyle(fontSize = 14.sp)) },
        hintText = "Кол-во калорий",
        style = TextStyle(fontSize = 18.sp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onValueChange = { onEvent(AddInfoEvent.CaloriesChange(it.formatAsFloat())) })
}

@Composable
private fun ProteinsTextField(state: FieldState, onEvent: (e: AddInfoEvent) -> Unit) {
    EATSTextField(
        value = state.value,
        error = state.error,
        label = { Text("Белки:", style = TextStyle(fontSize = 14.sp)) },
        hintText = "Кол-во белков",
        style = TextStyle(fontSize = 18.sp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onValueChange = { onEvent(AddInfoEvent.ProteinsChange(it.formatAsFloat())) })
}

@Composable
private fun FatsTextField(state: FieldState, onEvent: (e: AddInfoEvent) -> Unit) {
    EATSTextField(
        value = state.value,
        error = state.error,
        label = { Text("Жиры:", style = TextStyle(fontSize = 14.sp)) },
        hintText = "Кол-во жиров",
        style = TextStyle(fontSize = 18.sp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onValueChange = { onEvent(AddInfoEvent.FatsChange(it.formatAsFloat())) })
}

@Composable
private fun CarbohydratesTextField(state: FieldState, onEvent: (e: AddInfoEvent) -> Unit) {
    EATSTextField(
        value = state.value,
        error = state.error,
        label = { Text("Углеводы:", style = TextStyle(fontSize = 14.sp)) },
        hintText = "Кол-во углеводов",
        style = TextStyle(fontSize = 18.sp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onValueChange = { onEvent(AddInfoEvent.CarbohydratesChange(it.formatAsFloat())) })
}

@Composable
private fun ResultButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            "Добавить продукт",
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PortraitPreview() {
    EATSTheme {
        PortraitLayout(uiState = AddInfoState(), onEvent = {}, onResult = {})
    }
}

@Composable
@Preview(showBackground = true, device = "spec:shape=Normal,width=720,height=360,unit=dp,dpi=420")
private fun LandScapePreview() {
    EATSTheme {
        LandScapeLayout(uiState = AddInfoState(), onEvent = {}, onResult = {})
    }
}