package com.example.eats.navigation

import androidx.navigation.NavHostController
import com.example.eats.pages.eat.EatTime

class NavigationActions(private val navHostController: NavHostController) {
    fun navigateToEat(time: EatTime = EatTime.BreakFast) {
        navHostController.navigate("${Pages.Eat.name}?$TIME_ARG=${time.name}")
    }

    fun navigate(route: String) {
        navHostController.navigate(route)
    }
}