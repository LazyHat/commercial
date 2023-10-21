package ru.lazyhat.work.nutriguide.pages.eat.pages.eathome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.lazyhat.work.nutriguide.data.toProductState
import ru.lazyhat.work.nutriguide.pages.eat.EatTime
import ru.lazyhat.work.nutriguide.pages.eat.pages.ProductCard
import ru.lazyhat.work.nutriguide.pages.eat.pages.ProductDialog
import ru.lazyhat.work.nutriguide.pages.utils.SearchBar
import ru.lazyhat.work.nutriguide.pages.utils.TopBar

@Composable
fun EatHomePage(
    time: EatTime,
    onTimeChange: (new: EatTime) -> Unit,
    moveToAddProduct: () -> Unit,
    viewModel: EatHomeViewModel = eatHomeViewModel(time),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                currentTime = uiState.time,
                changeTime = {
                    viewModel.createEvent(EatHomeEvent.ChangeEatTime(it))
                    onTimeChange(it)
                })
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                onClick = moveToAddProduct
            ) {
                Text(
                    text = "Добавить продукт к ${
                        stringResource(uiState.time.label).lowercase()
                    }у",
                    style = TextStyle(fontSize = 27.sp)
                )
            }
            Text(
                text = "Список продуктов в ${
                    stringResource(uiState.time.label).lowercase()
                }е:",
                style = TextStyle(fontSize = 20.sp)
            )
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.searchValue,
                onValueChange = { viewModel.createEvent(EatHomeEvent.SearchValueChange(it)) })
            Column(
                Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                LazyColumn(
                    Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(uiState.products) {
                        ProductCard(
                            it.info,
                            it.weight,
                            openDialog = { dState ->
                                viewModel.createEvent(EatHomeEvent.OpenDialog(dState))
                            },
                            onDelete = { viewModel.createEvent(EatHomeEvent.DeleteProduct(it)) }
                        )
                    }
                }
            }
        }
    }
    uiState.productDialogState?.let {
        ProductDialog(
            state = it,
            onResult = { result -> viewModel.createEvent(EatHomeEvent.UpdateProduct(result.toProductState())) },
            onDismiss = { viewModel.createEvent(EatHomeEvent.DismissDialog) })
    }
}
