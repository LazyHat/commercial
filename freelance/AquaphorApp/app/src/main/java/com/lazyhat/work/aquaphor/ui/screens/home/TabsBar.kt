package com.lazyhat.work.aquaphor.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

//Нижний бар
@Composable
fun TabsBar(currentPageIndex: Int, moveToPage: (Int) -> Unit) {
    TabRow(
        selectedTabIndex = currentPageIndex,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        indicator = {}
    ) {
        HomePages.values().forEachIndexed { index, page ->
            Tab(
                page = page,
                selected = currentPageIndex == index,
                onClick = { moveToPage(index) })
        }
    }
}

@Composable
fun Tab(page: HomePages, selected: Boolean, onClick: () -> Unit) {
    Tab(selected = selected, onClick = onClick) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(5.dp)
        ) {
            Image(
                painterResource(id = page.tabImageRes),
                page.label,
                modifier = Modifier.size(35.dp)
            )
            Text(page.label)
        }
    }
}