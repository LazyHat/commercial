package com.lazyhat.work.tracker.ui.custom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazyhat.work.tracker.data.model.IpAddress
import com.lazyhat.work.tracker.ui.theme.TrackerTheme

@Composable
fun IpTextField(
    currentValue: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit = {},
    error: String? = null
) {
    BasicTextField(
        value = currentValue,
        onValueChange = onValueChange,
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        decorationBox = {
            Card(
                shape = RoundedCornerShape(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.2f))
            ) {
                Column(
                    Modifier.padding(5.dp)
                ) {
                    Row {
                        Text("set: ", color = Color.Black.copy(alpha = 0.5f))
                        it.invoke()
                    }
                    Divider(
                        Modifier.fillMaxWidth(),
                        2.dp,
                        color = if (error.isNullOrEmpty()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                    AnimatedVisibility(visible = !error.isNullOrEmpty()) {
                        Text(
                            error.orEmpty(),
                            color = MaterialTheme.colorScheme.error,
                            style = TextStyle(fontSize = 12.sp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
private fun PreviewIpTextField() {
    TrackerTheme {
        IpTextField(currentValue = IpAddress(255u, 255u, 255u, 255u).toString(), onValueChange = {})
    }
}