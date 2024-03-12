package com.example.mini_proj

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Home.route
        ) {
            HomeScreen(navController)
        }
        composable(
            route = Screen.Detail.route
        ) {
            DetailScreen(navController)
        }
        composable(
            route = Screen.Cart.route
        ) {
            ShoppingCartScreen(navController)
        }
        composable(
            route = Screen.Bill.route
        ) {
            ReceiptScreen(navController)
        }
        composable(
            route=Screen.Edit.route
        ){
            EditScreen(navController)
        }
    }
}
