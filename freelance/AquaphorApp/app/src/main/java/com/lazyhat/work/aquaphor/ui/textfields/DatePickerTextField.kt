package com.lazyhat.work.aquaphor.ui.textfields

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazyhat.work.aquaphor.R
import java.text.DateFormat
import java.util.Date

//Свой UI для выбора фильтра
@Composable
fun DatePickerTextField(
    currentValue: Long?,
    modifier: Modifier = Modifier,
    hint: String = "",
    fontSize: TextUnit = 14.sp,
    onOpen: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    LaunchedEffect(key1 = isPressed, block = {
        if (isPressed)
            onOpen()
    })
    CustomTextField(
        modifier = modifier,
        value = if (currentValue != null) DateFormat.getDateInstance()
            .format(Date(currentValue)) else "",
        hint = hint,
        fontSize = fontSize,
        readOnly = true,
        onValueChange = {},
        interactionSource = interactionSource,
        trailingIcon = {
            Icon(
                painterResource(id = R.drawable.calendar),
                "calendar_pick",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(5.dp)
                    .size(20.dp)
                    .clickable(onClick = onOpen)
            )
        })
}