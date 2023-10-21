package ru.lazyhat.work.nutriguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import ru.lazyhat.work.nutriguide.navigation.EATSNavGraph
import ru.lazyhat.work.nutriguide.navigation.NavigationActions
import ru.lazyhat.work.nutriguide.navigation.Pages
import ru.lazyhat.work.nutriguide.pages.eat.pages.addproduct.AddProductViewModel
import ru.lazyhat.work.nutriguide.pages.eat.pages.eathome.EatHomeViewModel
import ru.lazyhat.work.nutriguide.pages.utils.TabBar
import ru.lazyhat.work.nutriguide.theme.NutriGuideTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface ViewModelFactoryProvider {
        fun addProductViewModelFactory(): AddProductViewModel.Factory
        fun eatHomeViewModelFactory(): EatHomeViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutriGuideTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navActions = remember(navController) { NavigationActions(navController) }
    val currentPage = Pages.listNonNullPages().find {
        it.name == navController.currentBackStackEntryAsState().value?.destination?.route?.substringBefore(
            '?'
        )
    }?.ordinal ?: 0
    Scaffold(
        bottomBar = {
            TabBar(
                currentPageIndex = currentPage,
                navActions = navActions
            )
        }) { padding ->
        EATSNavGraph(
            modifier = Modifier.padding(padding),
            navController = navController,
            navActions = navActions
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    NutriGuideTheme {
        MainScreen()
    }
}