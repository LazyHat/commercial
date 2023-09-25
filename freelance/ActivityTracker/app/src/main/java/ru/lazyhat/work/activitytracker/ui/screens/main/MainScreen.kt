package ru.lazyhat.work.activitytracker.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.lazyhat.work.activitytracker.R
import ru.lazyhat.work.activitytracker.data.models.Contact
import ru.lazyhat.work.activitytracker.ui.screens.LoadingScreen
import ru.lazyhat.work.activitytracker.ui.screens.main.customui.AccuracyTextField
import ru.lazyhat.work.activitytracker.ui.screens.main.customui.ContactBox
import ru.lazyhat.work.activitytracker.ui.screens.main.customui.DurationTextField
import ru.lazyhat.work.activitytracker.ui.screens.main.customui.Finder
import ru.lazyhat.work.activitytracker.ui.theme.ActivityTrackerTheme
import ru.lazyhat.work.activitytracker.ui.theme.DarkGreen
import ru.lazyhat.work.activitytracker.ui.theme.DarkRed
import kotlin.time.DurationUnit

@Composable
fun MainScreen(
    vm: MainScreenViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    Screen(uiState, vm::getContacts) {
        vm.createEvent(it)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun Screen(
    uiState: MainScreenState,
    getContacts: (onReceived: (List<Contact>) -> Unit, onError: () -> Unit) -> Unit,
    onEvent: (e: MainScreenEvent) -> Unit
) {
    Scaffold(
        Modifier
            .fillMaxSize()
            .padding(5.dp),
        topBar = {
            Text(
                stringResource(id = R.string.app_name),
                modifier = Modifier
                    .padding(start = 20.dp, top = 20.dp, bottom = 10.dp),
                fontSize = 20.sp
            )
        },
        bottomBar = {
            Column() {
                AnimatedVisibility(visible = uiState.serviceEnabled) {
                    Column {
                        Text(
                            "Время бездействия: ${uiState.timeOfImmobility}",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        )
                    }
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        if (uiState.serviceEnabled)
                            onEvent(MainScreenEvent.StopService)
                        else
                            onEvent(MainScreenEvent.StartService)

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.serviceEnabled) DarkRed else DarkGreen
                    )
                ) {
                    Text(
                        if (uiState.serviceEnabled) "Остановить" else "Запустить",
                        modifier = Modifier.padding(10.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }) { scaffoldPadding ->
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(scaffoldPadding)
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                AccuracyTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = uiState.accuracyAccelerometer,
                    label = "Точность акселерометра"
                ) {
                    onEvent(MainScreenEvent.UpdateAccuracyAccelerometer(it))
                }
            }
            item {
                DurationTextField(
                    Modifier.fillMaxWidth(),
                    state = uiState.allowedTimeOfImmobility,
                    label = "Допуск по неподвижности:",
                    initialUnit = DurationUnit.HOURS
                ) {
                    onEvent(MainScreenEvent.UpdateAllowedTimeOfImmobility(it))
                }
            }
            item {
                DurationTextField(
                    Modifier.fillMaxWidth(),
                    state = uiState.messageRepeatingRate,
                    label = "Интервал посылки сообщений",
                    initialUnit = DurationUnit.MINUTES
                ) {
                    onEvent(MainScreenEvent.UpdateMessageRepeatingRate(it))
                }
            }
            item {
                DurationTextField(
                    Modifier.fillMaxWidth(),
                    state = uiState.sensorUpdateRate,
                    label = "Скорость обновления датчиков",
                    initialUnit = DurationUnit.SECONDS
                ) {
                    onEvent(MainScreenEvent.UpdateSensorUpdateRate(it))
                }
            }
            item {
                Spacer(Modifier.height(20.dp))
            }
            item {
                Text(modifier = Modifier.fillMaxWidth(), text = "Список контактов")
                Crossfade(targetState = uiState.savedContacts) {
                    Column {
                        it.forEachIndexed { index, contact ->
                            Column {
                                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                    ContactBox(modifier = Modifier.weight(0.8f), contact)
                                    IconButton(onClick = {
                                        onEvent(
                                            MainScreenEvent.DeleteContact(
                                                index
                                            )
                                        )
                                    }) {
                                        Icon(Icons.Default.Delete, null)
                                    }
                                }
                                Divider(thickness = 2.dp)
                            }
                        }
                        if (it.size < 3) {
                            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                                onEvent(MainScreenEvent.OpenAlertDialog)
                            }) {
                                Text("Добавить новый контакт (${it.size}/3)")
                            }
                        }
                    }
                }
            }
        }
    }
    if (uiState.isAlertDialogOpened) {
        AlertDialog(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f),
            onDismissRequest = { onEvent(MainScreenEvent.CloseAlertDialog) }
        ) {
            val availableContacts = remember { mutableStateOf<List<Contact>?>(null) }
            LaunchedEffect(key1 = Unit) {
                getContacts({ availableContacts.value = it }, {})
            }
            Card(
                border = BorderStroke(2.dp, Color.White), colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Crossfade(
                    modifier = Modifier.padding(20.dp),
                    targetState = availableContacts.value
                ) { contacts ->
                    if (contacts != null) {
                        var filter by remember { mutableStateOf("") }
                        Column() {
                            LazyColumn(modifier = Modifier.weight(0.9f)) {
                                item {
                                    Text(
                                        text = "Выберите нужный номер",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        textAlign = TextAlign.Center,
                                        fontSize = 16.sp
                                    )
                                }
                                stickyHeader {
                                    Finder(value = filter, onValueChange = { filter = it })
                                }
                                items(contacts.filter {
                                    it.name.lowercase()
                                        .contains(filter.lowercase()) || it.phone.lowercase()
                                        .contains(filter.lowercase())
                                }) {
                                    Column {
                                        ContactBox(
                                            modifier = Modifier.fillMaxWidth(),
                                            contact = it
                                        ) {
                                            onEvent(MainScreenEvent.AddNewContact(it))
                                            onEvent(MainScreenEvent.CloseAlertDialog)
                                        }
                                        Divider(thickness = 2.dp)
                                    }
                                }
                            }
                        }
                    } else LoadingScreen()
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ActivityTrackerTheme {
        Screen(
            MainScreenState.Default.copy(
                savedContacts = setOf(
                    Contact("rtewrwerew", "erwrerw"),
                    Contact("sdfew", "erwdfsrerw"),
                    //Contact("rdssdsderew", "edfffrw")
                )
            ),
            { _, _ -> }
        ) {}
    }
}