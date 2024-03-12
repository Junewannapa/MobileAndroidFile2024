package com.example.projecttable

sealed class Screen(val route: String, val name: String) {
    object TableHome : Screen(route = "tablehome_screen", name = "TableHome")
    object TableInsert : Screen(route = "tableinsert_screen", name = "TableInsert")
    object TableEdit:Screen(route = "tableedit_screen", name = "TableEdit")
}