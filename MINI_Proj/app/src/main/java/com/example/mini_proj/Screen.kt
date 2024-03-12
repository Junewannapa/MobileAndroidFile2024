package com.example.mini_proj
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.SetMeal
import androidx.compose.material.icons.filled.ShoppingBasket

sealed class Screen(val route: String, val name: String, val icon: ImageVector? = null) {
    object Home : Screen(route = "home_screen", name = "Home", icon = Icons.Default.Home)
    object Detail : Screen(route = "Detail_screen", name = "Menu", icon = Icons.Default.RestaurantMenu)
    object Cart : Screen(route = "cart_screen", name = "Cart",icon = Icons.Default.ShoppingBasket)
    object Bill : Screen(route = "bill_screen", name = "Bill",icon = Icons.Default.ReceiptLong)
    object Edit : Screen(route = "edit_screen",name="Add",icon = Icons.Default.ReceiptLong)
}
