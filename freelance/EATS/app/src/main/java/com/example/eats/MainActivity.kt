package com.example.eats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.eats.navigation.EATSNavGraph
import com.example.eats.navigation.NavigationActions
import com.example.eats.navigation.Pages
import com.example.eats.pages.eat.pages.addproduct.AddProductViewModel
import com.example.eats.pages.eat.pages.eathome.EatHomeViewModel
import com.example.eats.pages.utils.TabBar
import com.example.eats.ui.theme.EATSTheme
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent

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
            EATSTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
    EATSTheme {
        MainScreen()
    }
}