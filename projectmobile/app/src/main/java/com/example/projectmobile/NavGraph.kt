package com.example.projectmobile

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.InsertMenu.route
    ) {


        composable(
            route = Screen.Menu.route
        ) {
            MenuScreen(navController)
        }

        composable(
            route = Screen.InsertMenu.route
        ) {
            InsertMenuScreen(navController)
        }
        composable(
            route = Screen.InsertMenuType.route
        ) {
            InsertMenuTypeScreen(navController)
        }
        composable(
            route = Screen.EditMenu.route
        ) {
            EditMenuScreen(navController)
        }
    }
}
