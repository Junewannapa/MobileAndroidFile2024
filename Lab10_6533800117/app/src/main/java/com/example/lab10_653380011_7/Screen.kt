package com.example.lab10_653380011_7

sealed class Screen(val route: String, val name: String) {
    object Login: Screen(route = "login_screen", name = "Login")
    object Register: Screen(route = "register_screen", name = "Register")
    object Profile: Screen(route = "profile_screen", name = "Profile")
    object  Home: Screen(route = "home_screen", name = " Home")
}
