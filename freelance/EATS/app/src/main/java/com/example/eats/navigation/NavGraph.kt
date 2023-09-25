package com.example.eats.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.eats.R
import com.example.eats.pages.eat.EatScreen
import com.example.eats.pages.eat.EatTime
import com.example.eats.pages.home.HomeScreen
import com.example.eats.pages.recs.RecsPage
import com.example.eats.pages.user.UserPage
import com.example.eats.pages.water.WaterPage

enum class Pages(@StringRes val label: Int = 0, @DrawableRes val image: Int = 0) {
    Home(R.string.page_home_label, R.drawable.ic_home),
    Water(R.string.page_water_label, R.drawable.ic_water),
    Recs(R.string.page_recs_label, R.drawable.ic_recs),
    User(R.string.page_user_label, R.drawable.ic_user),
    Eat();

    companion object {
        fun listNonNullPages(): List<Pages> = Pages.values().filter { it.label != 0 || it.image != 0 }
    }
}
const val TIME_ARG = "time"

@Composable
fun EATSNavGraph(
    modifier: Modifier,
    startDestination: String = Pages.Home.name,
    navController: NavHostController = rememberNavController(),
    navActions: NavigationActions = NavigationActions(navController)
) {
    NavHost(
        modifier = modifier,
        startDestination = startDestination,
        navController = navController
    ) {
        composable(Pages.Home.name) {
           HomeScreen{ navActions.navigateToEat(it) }
        }
        composable(
            "${Pages.Eat.name}?$TIME_ARG={$TIME_ARG}",
            arguments = listOf(navArgument(TIME_ARG) {
                defaultValue = EatTime.BreakFast.name
                type = NavType.StringType
            })
        ) {
            EatScreen(EatTime.valueOf(it.arguments?.getString(TIME_ARG) ?: EatTime.BreakFast.name))
        }
        composable(Pages.Water.name) {
            WaterPage()
        }
        composable(Pages.Recs.name) {
            RecsPage()
        }
        composable(Pages.User.name) {
            UserPage()
        }
    }
}