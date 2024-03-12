package com.example.projectmobile


sealed class Screen (
    val route: String,
    val name: String,
    val iconName: String? = null  // Resource name without file extension
) {
    object Home : Screen(route = "home_screen", name = "Home", iconName = "home")
    object Table: Screen(route = "table_screen", name = "Table", iconName = "table1")
    object Menu: Screen(route = "menu_screen", name = "Menu", iconName = "")
    object Order: Screen(route = "Order_screen", name = "Order", iconName = "")
    object InsertMenu: Screen(route = "insert_screen", name = "")
    object InsertMenuType: Screen(route = "insert_type_screen", name = "Inserttype", iconName = "")
    object EditMenu: Screen(route = "edit_menu_screen", name = "EditMenu", iconName = "")
}