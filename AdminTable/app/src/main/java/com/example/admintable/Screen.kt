package com.example.admintable

sealed class Screen(val route: String,val name: String,val iconName: String){
    object TableHome: Screen(route = "table_home_screen", name = "Home", iconName = "home")
    object TableList: Screen(route = "table_list_screen", name = "Table", iconName = "table1")
    object TableInsert: Screen(route = "table_insert_screen", name = "TableInsert", iconName = "")
    object TableEdit: Screen(route = "table_edit_screen", name = "TableEdit", iconName = "")
    object AdminMenu: Screen(route = "admin_menu_screen", name = "Menu", iconName = "menu")
    object AdminOrder: Screen(route = "admin_order_screen", name = "Order", iconName = "order")
}
