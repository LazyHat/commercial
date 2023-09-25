package com.example.eats.pages.user

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eats.data.formatAsFloat
import com.example.eats.data.userdata.Activeness
import com.example.eats.data.userdata.Gender
import com.example.eats.navigation.Pages
import com.example.eats.pages.eat.FieldState
import com.example.eats.pages.utils.EATSTextField
import com.example.eats.pages.utils.TopBar
import com.example.eats.ui.theme.EATSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPage(viewModel: UserViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val onEvent: (e: UserEvent) -> Unit = { viewModel.createEvent(it) }
    var orientation by remember { mutableStateOf(Configuration.ORIENTATION_PORTRAIT) }
    val configuration = LocalConfiguration.current
    val scrollState = rememberScrollState()
    LaunchedEffect(key1 = configuration) {
        snapshotFlow { configuration.orientation }.collect { orientation = it }
    }
    Scaffold(topBar = { UserTopBar(onEvent) }) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
            when (orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> LandScapeLayout(
                    uiState = uiState,
                    onEvent = onEvent
                )

                else -> PortraitLayout(uiState = uiState, onEvent = onEvent)
            }
        }
    }
}

@Composable
private fun LandScapeLayout(uiState: UserState, onEvent: (e: UserEvent) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            Column(
                Modifier
                    .fillMaxHeight(0.88f)
                    .padding(20.dp), verticalArrangement = Arrangement.SpaceBetween
            ) {
                TextFieldHeight(state = uiState.height, onEvent = onEvent)
                TextFieldWeight(state = uiState.weight, onEvent = onEvent)
                TextFieldAge(state = uiState.age, onEvent = onEvent)
            }
            Column(
                Modifier
                    .fillMaxHeight(0.88f)
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GenderCheckBox(state = uiState.gender, onEvent = onEvent)
                DropDownActivityMenu(selectedItem = uiState.activeness, onEvent = onEvent)
            }
        }
        Text(text = stringResource(id = uiState.activeness.explanation))
    }
}

@Composable
private fun PortraitLayout(uiState: UserState, onEvent: (e: UserEvent) -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        TextFieldHeight(state = uiState.height, onEvent = onEvent)
        TextFieldAge(state = uiState.age, onEvent = onEvent)
        TextFieldWeight(state = uiState.weight, onEvent = onEvent)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            var width by remember { mutableStateOf(0.dp) }
            val ds = LocalDensity.current
            DropDownActivityMenu(
                Modifier.onGloballyPositioned {
                    width = with(ds) { it.size.width.toDp() }
                },
                selectedItem = uiState.activeness, onEvent = onEvent
            )
            Text(
                text =
                stringResource(id = uiState.activeness.explanation),
                modifier = Modifier.width(width)
            )
        }
        GenderCheckBox(state = uiState.gender, onEvent = onEvent)
        Text(text = "Параметры пользователя")
    }
}

@Composable
private fun TextFieldHeight(state: FieldState, onEvent: (e: UserEvent) -> Unit) {
    EATSTextField(
        value = state.value,
        onValueChange = {
            onEvent(UserEvent.UpdateHeight(it.formatAsFloat()))
        },
        hintText = "Введите ваш рост",
        error = state.error,
        label = { Text(text = "Рост:", style = TextStyle(fontSize = 13.sp)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
private fun TextFieldAge(state: FieldState, onEvent: (e: UserEvent) -> Unit) {
    EATSTextField(
        value = state.value,
        onValueChange = {
            if (it.isDigitsOnly())
                onEvent(UserEvent.UpdateAge(it))
        },
        hintText = "Введите ваш возраст",
        error = state.error,
        label = { Text(text = "Возраст:", style = TextStyle(fontSize = 13.sp)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
private fun TextFieldWeight(state: FieldState, onEvent: (e: UserEvent) -> Unit) {
    EATSTextField(
        value = state.value,
        onValueChange = {
            onEvent(UserEvent.UpdateWeight(it.formatAsFloat()))
        },
        hintText = "Введите ваш вес",
        error = state.error,
        label = { Text(text = "Вес:", style = TextStyle(fontSize = 13.sp)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
private fun GenderCheckBox(state: Gender, onEvent: (e: UserEvent) -> Unit) {
    Column {
        Text("Выберите ваш пол")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = state == Gender.Male, onCheckedChange = {
                if (it)
                    onEvent(UserEvent.UpdateGender(Gender.Male))
            })
            Text(text = "Мужской")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = state == Gender.Female, onCheckedChange = {
                if (it)
                    onEvent(UserEvent.UpdateGender(Gender.Female))
            })
            Text(text = "Женский")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownActivityMenu(
    modifier: Modifier = Modifier,
    selectedItem: Activeness,
    onEvent: (e: UserEvent) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }) {
        TextField(
            modifier = Modifier
                .menuAnchor(),
            value = selectedItem.label,
            readOnly = true,
            textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            onValueChange = {},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = selectedItem.color,
                textColor = MaterialTheme.colorScheme.background,
                focusedTrailingIconColor = MaterialTheme.colorScheme.background,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.background,
                placeholderColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                focusedIndicatorColor = MaterialTheme.colorScheme.background
            )
        )
        DropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            Activeness.values().forEach {
                DropdownMenuItem(
                    text = { Text(text = it.label) }, onClick = {
                        onEvent(UserEvent.UpdateActiveness(it))
                        expanded = false
                    })
            }
        }
    }
}

@Composable
private fun UserTopBar(onEvent: (e: UserEvent) -> Unit) {
    Box(Modifier.fillMaxWidth()) {
        TopBar(label = stringResource(Pages.User.label))
        Button(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(5.dp),
            onClick = { onEvent(UserEvent.Save) }) {
            Text(
                text = "СОХРАНИТЬ",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PortraitPreview() {
    EATSTheme {
        PortraitLayout(
            uiState = UserState(
                FieldState("180", ""),
                FieldState("25", ""),
                FieldState("80", "")
            ), onEvent = {}
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:shape=Normal,width=720,height=360,unit=dp,dpi=420"
)
@Composable
fun LandScapePreview() {
    EATSTheme {
        LandScapeLayout(
            uiState = UserState(
                FieldState("180", ""),
                FieldState("25", ""),
                FieldState("80", "")
            ), onEvent = {}
        )
    }
}