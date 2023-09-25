package ru.lazyhat.work.myhome.ui.screens.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import ru.lazyhat.work.myhome.R
import ru.lazyhat.work.myhome.data.models.cameras.CameraInfo
import ru.lazyhat.work.myhome.data.models.cameras.CameraRoom
import ru.lazyhat.work.myhome.data.models.doors.DoorInfo
import ru.lazyhat.work.myhome.ui.components.AnchoredDraggableBox
import ru.lazyhat.work.myhome.ui.screens.main.viewmodel.MainScreenEvent
import ru.lazyhat.work.myhome.ui.screens.main.viewmodel.MainScreenViewModel
import ru.lazyhat.work.myhome.ui.theme.MyHomeTheme
import ru.lazyhat.work.myhome.ui.theme.RecColor
import ru.lazyhat.work.myhome.ui.theme.StarColor

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterialApi::class
)
@Composable
fun MainScreen() {
    val vm: MainScreenViewModel = hiltViewModel()
    val uiState by vm.uiState.collectAsState()
    val onEvent: (MainScreenEvent) -> Unit = { vm.createEvent(it) }
    val pagerState = rememberPagerState(
        initialPage = 0, initialPageOffsetFraction = 0f
    ) { 2 }
    val pullRefreshState = rememberPullRefreshState(refreshing = uiState.refreshState.isLoading, {
        onEvent(MainScreenEvent.Refresh)
    })
    Scaffold(containerColor = MaterialTheme.colorScheme.background, topBar = {
        Column(Modifier.background(MaterialTheme.colorScheme.primary)) {
            Text(
                "Мой дом",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                textAlign = TextAlign.Center,
                fontSize = 25.sp,
                color = Color.Black
            )
            MainScreenTabRow(pagerState = pagerState)
        }
    }) { padding ->
        Box {
            HorizontalPager(
                modifier = Modifier
                    .padding(padding)
                    .pullRefresh(pullRefreshState),
                state = pagerState
            ) {
                Box(Modifier.fillMaxSize()) {
                    when (it) {
                        0 -> MainScreenCamerasPage(rooms = uiState.cameraRooms, onEvent)
                        1 -> MainScreenDoorsPage(doors = uiState.doors, onEvent)
                        else -> {
                            Text("USER SHOULDN'T SEE THIS TEXT")
                        }
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = uiState.refreshState.isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}


@Composable
fun MainScreenDoorsPage(doors: List<DoorInfo>, onEvent: (MainScreenEvent) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .padding(20.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(doors) {
            MainScreenSwipeableImageCard(bottomContent = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        it.name,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Icon(
                        painterResource(id = R.drawable.lock),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }, contentFirstPlan = {
                it.snapshot?.let {
                    AsyncImage(
                        modifier = Modifier.fillMaxWidth(), model = it, contentDescription = null
                    )
                    Icon(
                        painterResource(id = R.drawable.play_circle),
                        null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(80.dp),
                        tint = Color.White
                    )
                }
            }, swipedContent = {
                IconInCircle(
                    imageRes = R.drawable.rename,
                    iconColor = MaterialTheme.colorScheme.secondary
                ) {
                    /*TODO*/
                }
                Spacer(modifier = Modifier.width(10.dp))
                IconInCircle(
                    if (it.favorite) R.drawable.star else R.drawable.star_border,
                    iconColor = StarColor
                ) {
                    onEvent(MainScreenEvent.ChangeFavouriteDoor(it.id))
                }
            })
        }
    }
}


@Composable
fun MainScreenCamerasPage(rooms: List<CameraRoom>, onEvent: (MainScreenEvent) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .padding(20.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        rooms.forEach {
            it.name?.let {
                item {
                    Text(it, fontSize = 20.sp, modifier = Modifier.padding(vertical = 20.dp))
                }
            }
            items(it.cameras) {
                MainScreenSwipeableImageCard(bottomContent = {
                    Text(it.name, fontSize = 20.sp, color = Color.Black)
                }, contentFirstPlan = {
                    if (it.rec) Text(
                        "REC", color = RecColor, modifier = Modifier.padding(10.dp)
                    )
                    AsyncImage(
                        modifier = Modifier.fillMaxWidth(),
                        model = it.snapshot,
                        contentDescription = null
                    )
                    Icon(
                        painterResource(id = R.drawable.play_circle),
                        null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(80.dp),
                        tint = Color.White
                    )
                    Icon(
                        painter = painterResource(id = if (it.favorite) R.drawable.star else R.drawable.star_border),
                        contentDescription = null,
                        tint = StarColor,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                    )
                }, swipedContent = {
                    IconInCircle(
                        if (it.favorite) R.drawable.star else R.drawable.star_border,
                        iconColor = StarColor
                    ) {
                        onEvent(MainScreenEvent.ChangeFavouriteCamera(it.id))
                    }
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreenCamerasPage() {
    MyHomeTheme {
        MainScreenCamerasPage(rooms = listOf(
            CameraRoom(
                "FIRST", listOf(CameraInfo(1, "CAMERA 1", "", true, true))
            )
        ), onEvent = {})
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreenDoorsPage() {
    MyHomeTheme {
        MainScreenDoorsPage(doors = listOf(
            DoorInfo(
                "DOOR 1",
                null,
                1,
                true,
                null
            )
        ), onEvent = {})
    }
}

@Composable
fun MainScreenSwipeableImageCard(
    bottomContent: @Composable () -> Unit,
    contentFirstPlan: @Composable BoxScope.() -> Unit,
    swipedContent: @Composable RowScope.() -> Unit,
) {
    AnchoredDraggableBox(swipeContent = swipedContent) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column {
                Box {
                    contentFirstPlan()
                }
                Box(modifier = Modifier.padding(20.dp)) {
                    bottomContent()
                }
            }
        }
    }
}


@Composable
fun IconInCircle(imageVector: ImageVector, iconColor: Color, onClick: () -> Unit) {
    Card(
        shape = CircleShape,
        border = BorderStroke(0.dp, Color.Black),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .clickable(onClick = onClick)
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier
                    .padding(5.dp)
                    .size(25.dp),
                tint = iconColor
            )
        }
    }
}

@Composable
fun IconInCircle(@DrawableRes imageRes: Int, iconColor: Color, onClick: () -> Unit) {
    Card(
        shape = CircleShape,
        border = BorderStroke(0.dp, Color.Black),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .clickable(onClick = onClick)
        ) {
            Icon(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .padding(5.dp)
                    .size(25.dp),
                tint = iconColor
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewIconInCircle() {
    MyHomeTheme {
        IconInCircle(imageVector = Icons.Filled.Star, StarColor) {}
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreenTabRow(pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    TabRow(selectedTabIndex = pagerState.currentPage, indicator = {
        TabRowDefaults.Indicator(
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.tabIndicatorOffset(it[pagerState.currentPage])
        )
    }) {
        repeat(2) { tabIndex ->
            Tab(selected = pagerState.currentPage == tabIndex,
                onClick = { scope.launch { pagerState.animateScrollToPage(tabIndex) } },
                text = {
                    Text(
                        stringArrayResource(id = R.array.main_screen_tabs).let {
                            if (it.size - 1 < tabIndex) "Null"
                            else it[tabIndex]
                        },
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                })
        }
    }
}