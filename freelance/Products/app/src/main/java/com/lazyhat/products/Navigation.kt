package com.lazyhat.products

import androidx.navigation.NavHostController
import com.lazyhat.products.data.ProductPage

const val MAIN_ROUTE = "main"

class NavigationActions(private val navHostController: NavHostController) {
    fun navigateToProductsPage(page: ProductPage) {
        navHostController.navigate(page.name)
    }

    fun navigateUp() = navHostController.navigateUp()
}