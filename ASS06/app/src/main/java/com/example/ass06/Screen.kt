package com.example.ass06

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Face4
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen (val route:String,val name:String,val icon:ImageVector) {
    object Home: Screen(route = "home_screen",name="Home", icon = Icons.Default.Home)
    object Friend01: Screen(route = "friend01_screen",name="Friend 1", icon = Icons.Default.Face)
    object Friend02: Screen(route = "favorite_screen",name=" Friend 2", icon = Icons.Default.Face4)
}