package com.example.ass06

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun Nav(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ){
        composable(
            route = Screen.Home.route
        ){
            Home()
        }
        composable(
            route = Screen.Friend01.route
        ){
            Friend01()
        }

        composable(
            route = Screen.Friend02.route
        ){
            Friend02()
        }
    }
}
