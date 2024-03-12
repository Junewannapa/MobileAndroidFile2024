package com.example.pjtable

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.TableHome.route
    ) {
        composable(
            route = Screen.TableHome.route
        ) {
            TableHomeScreen(navController)
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
    }
}