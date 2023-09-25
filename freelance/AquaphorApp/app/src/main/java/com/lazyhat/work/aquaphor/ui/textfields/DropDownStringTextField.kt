package com.lazyhat.work.aquaphor.ui.textfields

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazyhat.work.aquaphor.R
import com.lazyhat.work.aquaphor.ui.theme.RobotoFamily

//Свой UI для выбора даты
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownStringTextField(
    items: List<String>,
    currentValue: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    fontSize: TextUnit = 14.sp
) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        CustomTextField(
            value = currentValue,
            onValueChange = {},
            hint = hint,
            modifier = modifier.menuAnchor(),
            fontSize = fontSize,
            readOnly = true,
            trailingIcon = {
                Icon(
                    painterResource(id = R.drawable.dropdown_string),
                    "dropdown",
                    modifier = Modifier
                        .padding(5.dp)
                        .size(20.dp)
                        .rotate(rotation),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach {
                DropdownMenuItem(text = {
                    Text(
                        it,
                        style = TextStyle(
                            fontFamily = RobotoFamily,
                            fontWeight = FontWeight.W400,
                            fontSize = fontSize
                        )
                    )
                }, onClick = {
                    onValueChange(it)
                    expanded = false
                })
            }
        }
    }
}