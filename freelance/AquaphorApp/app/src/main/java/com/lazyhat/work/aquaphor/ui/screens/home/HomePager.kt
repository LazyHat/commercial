package com.lazyhat.work.aquaphor.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.lazyhat.work.aquaphor.ui.screens.home.pages.AboutPage
import com.lazyhat.work.aquaphor.ui.screens.home.pages.FilterPage
import com.lazyhat.work.aquaphor.ui.screens.home.pages.InstructionPage
import kotlinx.coroutines.launch

//Пейджер для главного экрана, заведует всеми страницами кроме регистрации
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePager(
    viewModel: HomePagerViewModel = hiltViewModel(),
    returnToRegisterPage: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(initialPage = 1) { HomePages.values().size }
    val scope = rememberCoroutineScope()
    Scaffold(bottomBar = {
        TabsBar(currentPageIndex = pagerState.currentPage, moveToPage = {
            scope.launch {
                pagerState.animateScrollToPage(it)
            }
        })
    }) {
        HorizontalPager(
            modifier = Modifier.padding(it),
            state = pagerState
        ) { page ->
            when (page) {
                0 -> {
                    InstructionPage(filter = uiState)
                }

                1 -> {
                    FilterPage(filter = uiState, toRegister = returnToRegisterPage)
                }

                2 -> {
                    AboutPage()
                }

                else -> {
                    Box(Modifier.fillMaxSize()) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center,
                            text = "page not found"
                        )
                    }
                }
            }
        }
    }
}