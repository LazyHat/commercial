package com.lazyhat.work.aquaphor.ui.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazyhat.work.aquaphor.ui.theme.AquaphorTheme
import com.lazyhat.work.aquaphor.ui.theme.RobotoFamily

//Свой UI для текстовых полей
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    fontSize: TextUnit = 14.sp,
    color: Color = MaterialTheme.colorScheme.primary,
    trailingIcon: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    readOnly: Boolean = false
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            fontFamily = RobotoFamily,
            fontWeight = FontWeight.W400,
            fontSize = fontSize
        ),
        interactionSource = interactionSource,
        readOnly = readOnly,
        singleLine = true
    ) {
        Column(modifier = modifier) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box {
                    if (value.isEmpty()) {
                        Text(
                            hint,
                            style = TextStyle(
                                fontSize = fontSize,
                                fontWeight = FontWeight.W200,
                                fontFamily = RobotoFamily
                            )
                        )
                    }
                    it.invoke()
                }
                trailingIcon?.invoke()
            }
            Spacer(Modifier.height(3.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(color)
                    .height(2.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCustomTextField() {
    AquaphorTheme {
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            hint = "Выбрите фильтр",
            fontSize = 25.sp,
            color = Color.Red,
            value = "",
            onValueChange = {}
        )
    }
}