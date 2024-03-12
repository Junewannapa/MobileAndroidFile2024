package com.example.pjtabletap

sealed class Screen (
    val route: String,
    val name: String,
    val iconName: String? = null  // Resource name without file extension
) {

    object Menu: Screen(route = "menu_screen", name = "Menu", iconName = "dish")
    object InsertMenu: Screen(route = "insert_screen", name = "Insert")
    object InsertMenuType: Screen(route = "insert_type_screen", name = "Inserttype", iconName = "insert_type_icon")
    object EditMenu: Screen(route = "edit_menu_screen", name = "EditMenu", iconName = "edit_menu_icon")
}