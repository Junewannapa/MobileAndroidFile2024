package com.example.tabletap

sealed class Screen(val route: String,val name: String,val iconName: String){
    object Login: Screen(route = "login_screen", name = "Login", iconName = "")
    object Register: Screen(route = "register_screen", name = "Register", iconName = "")
    object Home: Screen(route = "home_screen", name = "Home", iconName = "home")
    object Table: Screen(route = "table_screen", name = "Table", iconName = "table1")
    object TableInsert: Screen(route = "table_insert_screen", name = "TableInsert", iconName = "")
    object TableEdit: Screen(route = "table_edit_screen", name = "TableEdit", iconName = "")
    object BillAdmin: Screen(route = "bill_admin_screen/{tableId}", name = "BillAdmin", iconName = "")
//    object HomeUser : Screen(route = "home_user_screen", name = "Home", iconName = "home")
//    object Detail : Screen(route = "Detail_screen", name = "Menu", iconName = "")
//    object Cart : Screen(route = "cart_screen", name = "Cart",iconName = "")
//    object Bill : Screen(route = "bill_screen", name = "Bill",iconName = "")
//    object Edit: Screen(route = "edit_screen", name="Add",iconName = "")
//    object Menu: Screen(route = "menu_screen", name = "Menu", iconName = "")
//    object Order: Screen(route = "order_screen", name = "Order", iconName = "")
//    object User: Screen(route = "user_screen", name = "User", iconName = "")
}
