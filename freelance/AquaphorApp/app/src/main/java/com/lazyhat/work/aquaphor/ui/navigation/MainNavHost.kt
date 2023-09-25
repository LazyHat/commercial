package com.lazyhat.work.aquaphor.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lazyhat.work.aquaphor.ui.screens.home.HomePager
import com.lazyhat.work.aquaphor.ui.screens.register.RegisterScreen

//Граф нафигации

@Composable
fun MainNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: MainNavRoutes = MainNavRoutes.Register,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route
    ) {
        composable(MainNavRoutes.Home.route) {
            HomePager{
                navController.navigate(MainNavRoutes.Register.route) {
                    popUpTo(MainNavRoutes.Home.route) {
                        inclusive = true
                    }
                }
            }
        }
        composable(MainNavRoutes.Register.route) {
            RegisterScreen {
                navController.navigate(MainNavRoutes.Home.route) {
                    popUpTo(MainNavRoutes.Register.route) {
                        inclusive = true
                    }
                }
            }
        }
    }
}