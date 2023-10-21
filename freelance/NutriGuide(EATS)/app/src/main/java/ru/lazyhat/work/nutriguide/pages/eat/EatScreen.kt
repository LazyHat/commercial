package ru.lazyhat.work.nutriguide.pages.eat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.lazyhat.work.nutriguide.pages.eat.pages.addinfo.AddInfoPage
import ru.lazyhat.work.nutriguide.pages.eat.pages.addproduct.AddProductPage
import ru.lazyhat.work.nutriguide.pages.eat.pages.eathome.EatHomePage

enum class EatPages {
    Home,
    AddProduct,
    AddInfo
}

@Composable
fun EatScreen(
    time: EatTime,
    navController: NavHostController = rememberNavController(),
    viewModel: EatScreenViewModel = EatScreenViewModel(time)
) {
    val uiState by viewModel.uiState.collectAsState()
    NavHost(
        navController = navController,
        startDestination = EatPages.Home.name
    ) {
        composable(EatPages.Home.name) {
            EatHomePage(time = uiState.time,
                onTimeChange = { viewModel.createEvent(EatScreenEvent.UpdateTime(it)) },
                moveToAddProduct = {
                    navController.navigate(
                        EatPages.AddProduct.name
                    )
                })
        }
        composable(EatPages.AddProduct.name) {
            AddProductPage(
                uiState.time,
                onResult = { navController.navigateUp() },
                moveToAddInfo = { navController.navigate(EatPages.AddInfo.name) })
        }
        composable(EatPages.AddInfo.name) {
            AddInfoPage(){navController.navigateUp()}
        }
    }
}