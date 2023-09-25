package com.lazyhat.products

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lazyhat.products.data.ProductPage
import com.lazyhat.products.screens.MainScreen
import com.lazyhat.products.screens.pages.ProductsPage
import com.lazyhat.products.ui.theme.ProductsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductsTheme {
                val navHostController = rememberNavController()
                val navigationActions = NavigationActions(navHostController)
                NavHost(navController = navHostController, startDestination = MAIN_ROUTE) {
                    composable(MAIN_ROUTE) {
                        MainScreen(navigationActions)
                    }
                    ProductPage.values().forEach {
                        composable(it.name) { _ ->
                            ProductsPage(
                                pageInfo = it,
                                back = { navigationActions.navigateUp() })
                        }
                    }
                }
            }
        }
    }
}