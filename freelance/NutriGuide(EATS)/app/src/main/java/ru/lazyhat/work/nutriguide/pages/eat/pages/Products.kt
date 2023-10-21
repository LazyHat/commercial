package ru.lazyhat.work.nutriguide.pages.eat.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.lazyhat.work.nutriguide.data.formatAsFloat
import ru.lazyhat.work.nutriguide.data.getCurrentNutrition
import ru.lazyhat.work.nutriguide.data.products.ProductInfo
import ru.lazyhat.work.nutriguide.staticdata.DataSource
import ru.lazyhat.work.nutriguide.staticdata.DataSource.df
import ru.lazyhat.work.nutriguide.theme.NutriGuideTheme


@Composable
fun ProductCard(
    info: ProductInfo,
    weight: Float? = null,
    openDialog: (productDialogState: ProductDialogState) -> Unit,
    onDelete: (() -> Unit)? = null
) {
    Card(
        Modifier
            .fillMaxWidth()
            .clickable { openDialog(ProductDialogState(info, weight)) },
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = info.label,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(start = 5.dp)
                )
                Text(
                    text = "${if (weight != null) df.format(weight) else ""} гр.",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(start = 5.dp)
                )
                onDelete?.let {
                    Text(
                        text = "Удалить",
                        color = Color.Red,
                        modifier = Modifier.clickable { it() })
                }
            }
            Spacer(Modifier.height(4.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                val nutritionFacts =
                    weight?.let { info.nutrition100.getCurrentNutrition(it) } ?: info.nutrition100
                Text(text = "Калории: ${df.format(nutritionFacts.calories)}")
                Text(text = "Б: ${df.format(nutritionFacts.proteins)} ")
                Text(text = "Ж: ${df.format(nutritionFacts.fats)} ")
                Text(text = "У: ${df.format(nutritionFacts.carbohydrates)} ")
            }
        }
    }
}

data class ProductDialogState(val info: ProductInfo, val weight: Float? = null)
data class ResultDialogState(val info: ProductInfo, val weight: Float)

@Composable
fun ProductDialog(
    state: ProductDialogState,
    onResult: (state: ResultDialogState) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = Modifier,
        onDismissRequest = onDismiss,
        textContentColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.background,
        title = { Text(text = state.info.label) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(text = "Пищевая ценность на 100 грамм:")
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    Text(text = "Калории: ${df.format(state.info.nutrition100.calories)}")
                    Text(text = "Б: ${df.format(state.info.nutrition100.proteins)} ")
                    Text(text = "Ж: ${df.format(state.info.nutrition100.fats)} ")
                    Text(text = "У: ${df.format(state.info.nutrition100.carbohydrates)} ")
                }
            }
        },
        confirmButton = {
            var weight by remember { mutableStateOf(state.weight?.toString() ?: "") }
            OutlinedTextField(
                value = weight,
                onValueChange = {
                    weight = it.formatAsFloat()
                },
                trailingIcon = {
                    Button(
                        modifier = Modifier.padding(end = 10.dp),
                        onClick = {
                            if (weight.isNotEmpty()) {
                                onResult(ResultDialogState(state.info, weight.toFloat()))
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(5.dp)
                    ) { Text(text = if (state.weight == null) "Добавить" else "Изменить") }
                },
                label = { Text(text = "Укажите граммовку", style = TextStyle(fontSize = 12.sp)) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedLabelColor = MaterialTheme.colorScheme.background,
                    focusedLabelColor = MaterialTheme.colorScheme.background,
                    unfocusedBorderColor = MaterialTheme.colorScheme.background,
                    focusedTextColor = MaterialTheme.colorScheme.background
                )
            )
        }
    )
}

@Preview
@Composable
fun PreviewProductDialog() {
    NutriGuideTheme {
        ProductDialog(
            state = ProductDialogState(DataSource.productInfos[0]),
            onResult = { /*TODO*/ }, {})
    }
}

@Preview
@Composable
fun PreviewProduct() {
    NutriGuideTheme {
        ProductCard(
            DataSource.productInfos[0],
            50f,
            {}, {}
        )
    }
}

