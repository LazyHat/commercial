package com.example.eats.pages.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eats.R
import com.example.eats.data.toString
import com.example.eats.pages.eat.EatTime
import com.example.eats.pages.utils.TopBar
import com.example.eats.staticdata.DataSource.df
import com.example.eats.ui.theme.EATSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    uiState: State<HomeState> = (hiltViewModel() as HomePageViewModel).uiState.collectAsState(),
    moveToEat: ((time: EatTime) -> Unit)?
) {
    Scaffold(topBar = {
        TopBar(label = "${stringResource(id = R.string.page_home_label)} - ${uiState.value.date.toString()}")
    }) { padding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(10.dp)
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                CaloriesCard(
                    uiState.value.caloriesDay,
                    uiState.value.needCalories,
                    uiState.value.currentCalories
                )
            }

            items(uiState.value.eatBoxes) {
                EatTimeCard(it, moveToEat)
            }
        }
    }
}

@Composable
private fun CaloriesCard(
    caloriesDay: Float,
    caloriesNeed: Float,
    caloriesCurrent: Float
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Суточная норма: ${df.format(caloriesDay)} ккал",
                style = TextStyle(fontSize = 19.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(
                        modifier = Modifier.padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Съедено ккал: \n${df.format(caloriesCurrent)}",
                            style = TextStyle(fontSize = 19.sp),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.background
                        )
                    }
                }
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(
                        modifier = Modifier.padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Осталось ккал: \n${
                                if (caloriesNeed <= 0) "Выполнено" else df.format(
                                    caloriesNeed
                                )
                            }",
                            style = TextStyle(fontSize = 19.sp),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.background
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EatTimeCard(state: HomeEatBoxState, onClickBox: ((time: EatTime) -> Unit)?) {
    Card(
        Modifier
            .fillMaxWidth()
            .height(80.dp).let {
                if (onClickBox != null)
                    it.clickable { onClickBox.invoke(state.time) }
                else it
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(5.dp)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 5.dp, top = 3.dp)
        ) {
            Column {
                Text(
                    text = stringResource(id = state.time.label),
                    color = MaterialTheme.colorScheme.background,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.ExtraBold),
                    modifier = Modifier.padding(bottom = 5.dp, top = 10.dp)
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "${df.format(state.percent)}% от сут. потребления (${df.format(state.calories)} ккал)",
                    color = MaterialTheme.colorScheme.background,
                    softWrap = true
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewEatTimeCard() {
    EATSTheme {
        EatTimeCard(state = HomeEatBoxState(EatTime.BreakFast, 20.3f, 400f), onClickBox = {})
    }
}

@Preview
@Composable
fun PreviewCaloriesCard() {
    EATSTheme {
        CaloriesCard(2500f, 545.5f, 1954.5f)
    }
}