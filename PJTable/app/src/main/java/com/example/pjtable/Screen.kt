package com.example.pjtable

sealed class Screen(val route: String, val name: String) {
    object TableHome : Screen(route = "home_screen", name = "TableHome")
    object TableInsert : Screen(route = "insert_screen", name = "TableInsert")
    object TableEdit:Screen(route = "edit_screen", name = "TableEdit")
}

