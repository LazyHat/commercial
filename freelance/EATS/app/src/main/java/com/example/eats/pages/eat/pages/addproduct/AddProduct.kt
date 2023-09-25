package com.example.eats.pages.eat.pages.addproduct

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eats.data.toProductState
import com.example.eats.pages.eat.EatTime
import com.example.eats.pages.eat.pages.ProductCard
import com.example.eats.pages.eat.pages.ProductDialog
import com.example.eats.pages.utils.SearchBar

@Composable
fun AddProductPage(
    eatTime: EatTime,
    viewModel: AddProductViewModel = addProductViewModel(time = eatTime),
    onResult: () -> Unit,
    moveToAddInfo: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    Column(
        Modifier
            .fillMaxSize()
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Добавление продукта к ${stringResource(id = uiState.time.label)}у",
            style = TextStyle(fontSize = 25.sp),
            modifier = Modifier.padding(top = 10.dp)
        )
        Text(text = "Выберите продукт из списка: ")
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.searchValue,
            onValueChange = { viewModel.createEvent(AddProductEvent.SearchValueChange(it)) })
        var buttonY by remember { mutableStateOf(0.dp) }
        val ds = LocalDensity.current
        Box(
            Modifier.fillMaxSize(),
        ) {
            LazyColumn(
                Modifier
                    .padding(bottom = buttonY)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(uiState.availableProducts) {
                    ProductCard(
                        info = it, openDialog = { dState ->
                            viewModel.createEvent(AddProductEvent.OpenDialog(dState))
                        })
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .onGloballyPositioned {
                        buttonY = with(ds) { it.size.height.toDp() }
                    },
                shape = RoundedCornerShape(5.dp),
                onClick = moveToAddInfo
            ) {
                Text(
                    text = "Добавить новый продукт",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                )
            }
        }
    }
    if (uiState.productDialogState != null) {
        ProductDialog(
            state = uiState.productDialogState!!,
            onResult = {
                viewModel.createEvent(AddProductEvent.InsertProduct(it.toProductState()))
                onResult()
            },
            onDismiss = { viewModel.createEvent(AddProductEvent.DismissDialog) })
    }
}