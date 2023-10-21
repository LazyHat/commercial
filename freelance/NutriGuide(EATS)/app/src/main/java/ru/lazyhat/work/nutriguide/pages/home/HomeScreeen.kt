package ru.lazyhat.work.nutriguide.pages.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ru.lazyhat.work.nutriguide.pages.eat.EatTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    moveToEat: (time: EatTime) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    Column() {
        HorizontalPager(
            state = rememberPagerState(0),
            reverseLayout = true,
            pageCount = uiState.homePagesPrevious.size + 1
        ) {
            if (it == 0)
                HomePage(moveToEat = moveToEat)
            else
                HomePage(
                    moveToEat = null,
                    uiState = MutableStateFlow(uiState.homePagesPrevious[it-1]).collectAsState()
                )
        }
    }
}