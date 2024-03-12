package com.example.tabletap

import androidx.compose.runtime.Composable
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
            route = Screen.Table.route
        ) {
            TableScreen(navController)
        }
        composable(
            route = Screen.TableInsert.route
        ) {
            TableInsertScreen(navController)
        }
        composable(
            route = Screen.TableEdit.route
        ) {
            TableEditScreen(navController)
        }
        composable(route = Screen.BillAdmin.route) { backStackEntry ->
            val tableId = backStackEntry.arguments?.getString("tableId")
            BillAdminScreen(navController, tableId)
        }

//        composable(
//            route = Screen.HomeUser.route
//        ) {
//            HomeUserScreen(navController)
//        }
//
//        composable(
//            route = Screen.Detail.route
//        ) {
//            DetailScreen(navController)
//        }
//        composable(
//            route = Screen.Cart.route
//        ) {
//            CartScreen(navController)
//        }
//        composable(
//            route = Screen.Bill.route
//        ) {
//            BillScreen(navController)
//        }
//        composable(
//            route=Screen.Edit.route
//        ){
//            EditUserScreen(navController)
//        }
//        composable(
//            route = Screen.Menu.route
//        ) {
//            MenuFoodScreen(navController)
//        }
//        composable(
//            route = Screen.Order.route
//        ) {
//            OrderScreen(navController)
//        }
//        composable(
//            route = Screen.User.route
//        ) {
//            UserScreen(navController)
//        }

    }
}