package ru.lazyhat.work.activitytracker.ui.screens.main.customui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.lazyhat.work.activitytracker.ui.screens.main.TextFieldState
import ru.lazyhat.work.activitytracker.ui.theme.ActivityTrackerTheme
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DurationTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState<Duration>,
    label: String,
    initialUnit: DurationUnit,
    onValueChange: (Duration) -> Unit
) {
    var durationUnit by remember { mutableStateOf(initialUnit) }
    val valueString = state.value.toLong(durationUnit).toString()
    val keyboardController = LocalSoftwareKeyboardController.current
    var textState by remember(valueString) { mutableStateOf(valueString) }
    TextField(
        modifier = modifier,
        value = textState,
        onValueChange = { str ->
            textState = str
        },
        label = {
            Text(label, fontSize = 14.sp)
        },
        trailingIcon = {
            Row(
                modifier = Modifier.padding(start = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(durationUnit.name, modifier = Modifier.clickable {
                    durationUnit = when (durationUnit) {
                        DurationUnit.NANOSECONDS -> DurationUnit.SECONDS
                        DurationUnit.MICROSECONDS -> DurationUnit.SECONDS
                        DurationUnit.MILLISECONDS -> DurationUnit.SECONDS
                        DurationUnit.SECONDS -> DurationUnit.MINUTES
                        DurationUnit.MINUTES -> DurationUnit.HOURS
                        DurationUnit.HOURS -> DurationUnit.DAYS
                        DurationUnit.DAYS -> DurationUnit.SECONDS
                    }
                })
                AnimatedVisibility(visible = textState != valueString) {
                    IconButton(
                        onClick = {
                            textState = valueString
                            keyboardController?.hide()
                        }
                    ) {
                        Icon(Icons.Default.Refresh, null)
                    }
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrect = false,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            textState.ifBlank { "0" }.toLongOrNull()?.toDuration(durationUnit)
                ?.takeIf { it != Duration.INFINITE }
                ?.let {
                    onValueChange(it)
                    keyboardController?.hide()
                }
        }),
        singleLine = true,
        maxLines = 1,
        supportingText = { Text("Текущее: ${state.value}") }
    )
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    ActivityTrackerTheme {
        DurationTextField(
            state = TextFieldState(25.hours, null),
            label = "Preview label durationtextfield",
            initialUnit = DurationUnit.MINUTES,
            onValueChange = {})
    }
}