package com.lazyhat.work.tracker.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lazyhat.work.tracker.data.Utils.df
import com.lazyhat.work.tracker.data.format
import com.lazyhat.work.tracker.ui.custom.IpTextField
import com.lazyhat.work.tracker.ui.theme.button
import java.sql.Time
import java.util.concurrent.TimeUnit
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(vm: MainVM = hiltViewModel()) {
    val uiState by vm.uiState.collectAsState()
    var alertOpened by remember { mutableStateOf(false) }
    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("saved ip: ${uiState.savedIp}")
        IpTextField(
            currentValue = uiState.currentIp,
            onValueChange = { vm.createEvent(MainEvent.UpdateIp(it)) },
            onDone = { vm.createEvent(MainEvent.SaveIp) },
            error = uiState.error
        )
        Text("battery level: ${uiState.batteryLevel}")
        Column {
            Text("lat: ${uiState.lat.format(df)}")
            Text("long: ${uiState.long.format(df)}")
        }
        Row {
            Text("status: ")
            Text(
                if (uiState.status) "Active" else "idle",
                color = if (uiState.status) Color.Green else Color.Gray
            )
        }
        Button(
            onClick = { vm.createEvent(MainEvent.RunSender) },
            shape = MaterialTheme.shapes.button
        ) {
            Text("Run sender")
        }
        Button(
            onClick = { vm.createEvent(MainEvent.StopSender) },
            shape = MaterialTheme.shapes.button
        ) {
            Text("Stop sender")
        }
        Button(
            onClick = { alertOpened = true },
            shape = MaterialTheme.shapes.button
        ) {
            Text("Choose send speed")
        }
        Row {
            if (uiState.imageBack != null)
                Image(
                    modifier = Modifier.size(50.dp),
                    bitmap = uiState.imageBack!!,
                    contentDescription = null
                )
            if (uiState.imageFront != null)
                Image(
                    modifier = Modifier.size(50.dp),
                    bitmap = uiState.imageFront!!,
                    contentDescription = null
                )
        }
        Text(text = "real interval: ${uiState.updateInterval / 1000.0f} ")
    }
    if (alertOpened)
        AlertDialog(
            onDismissRequest = { alertOpened = false }) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                var currentDurationUnit by remember {
                    mutableStateOf(uiState.durationUnit)
                }
                var currentDuration by remember {
                    mutableLongStateOf(uiState.duration)
                }
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Time units")
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            enabled = currentDurationUnit.ordinal > 0, onClick = {
                                currentDurationUnit =
                                    TimeUnit.values()[currentDurationUnit.ordinal - 1]
                            }, shape = MaterialTheme.shapes.button
                        ) { Text("<") }
                        Text(
                            modifier = Modifier
                                .border(
                                    2.dp,
                                    SolidColor(MaterialTheme.colorScheme.primary),
                                    shape = MaterialTheme.shapes.button
                                )
                                .padding(5.dp)
                                .fillMaxWidth(),
                            text = currentDurationUnit.name,
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            enabled = currentDurationUnit.ordinal < TimeUnit.DAYS.ordinal,
                            onClick = {
                                currentDurationUnit =
                                    TimeUnit.values()[currentDurationUnit.ordinal + 1]
                            },
                            shape = MaterialTheme.shapes.button
                        ) { Text(">") }
                    }
                    OutlinedTextField(
                        label = { Text("Duration") },
                        value = currentDuration.toString(), onValueChange = { str ->
                            if (str.all { it.isDigit() }.and(str.isNotEmpty()))
                                currentDuration = str.toLong()
                        })
                    Button(
                        shape = MaterialTheme.shapes.button,
                        onClick = {
                            alertOpened = false
                            vm.createEvent(
                                MainEvent.UpdateDuration(
                                    currentDuration,
                                    currentDurationUnit
                                )
                            )
                        }) { Text("Set") }
                }
            }
        }
}


