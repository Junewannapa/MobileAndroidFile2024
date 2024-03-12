package com.example.tabletap

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tabletap.ui.theme.TableTapTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TableTapTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TableTapTheme {
        Greeting("Android")
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun MyAdminBottomBar(navController: NavHostController) {
    val navigationItems = listOf(
        Screen.Home,
        Screen.Table,
//        Screen.Menu,
//        Screen.Order
    )
    var selectedScreen by remember {
        mutableIntStateOf(0)
    }
    val resources = LocalContext.current.resources
    if( navController.currentDestination?.route == Screen.Login.route){

    }else{
    NavigationBar(
        containerColor = Color(0xFFFF3509)
    ) {
        navigationItems.forEachIndexed{ index, screen ->
            NavigationBarItem(
                icon = {
                    val resourceId =
                        resources.getIdentifier(
                            screen.iconName,
                            "drawable",
                            "com.example.tabletap"
                        )
                    Icon(
                        painter = painterResource(id = resourceId),
                        contentDescription = null,
                        tint = if (selectedScreen == index) Color(0xFFFF3509) else Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                },
                label = { Text(text = screen.name,
                    color = Color.White
                )},
                selected = (selectedScreen == index),
                onClick = {
                    if (navController.currentBackStack.value.size >= 2) {
                        navController.popBackStack()
                    }
                    selectedScreen = index
                    navController.navigate(screen.route)
                }
            )
        }
    } }
}
@SuppressLint("RestrictedApi")
@Composable
fun MyUserBottomBar(navController: NavHostController) {
    val navigationItems = listOf(
        Screen.Home,
        Screen.Table,
//        Screen.Menu,


    )
    var selectedScreen by remember {
        mutableIntStateOf(0)
    }
    val resources = LocalContext.current.resources
    if( navController.currentDestination?.route == Screen.Login.route){

    }else{
        NavigationBar(
            containerColor = Color(0xFFFF3509)
        ) {
            navigationItems.forEachIndexed{ index, screen ->

                NavigationBarItem(
//                modifier = Modifier.background(Color(0xFFFF3509)),
                    icon = {
                        val resourceId =
                            resources.getIdentifier(
                                screen.iconName,
                                "drawable",
                                "com.example.tabletap"
                            )
                        Icon(
                            painter = painterResource(id = resourceId),
                            contentDescription = null,
                            tint = if (selectedScreen == index) Color(0xFFFF3509) else Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    },
                    label = { Text(text = screen.name,
                        color = Color.White
                    )},
                    selected = (selectedScreen == index),
                    onClick = {
                        if (navController.currentBackStack.value.size >= 2) {
                            navController.popBackStack()
                        }
                        selectedScreen = index
                        navController.navigate(screen.route)
                    }
                )
            }
        } }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyUI(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val drawerItemList = prepareNavigationDrawerItems()
    val selectedItem = remember { mutableStateOf(drawerItemList[0]) }
    var logoutDialog by remember { mutableStateOf(false) }

    DismissibleNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DismissibleDrawerSheet {
                Spacer(Modifier.height(12.dp))
                drawerItemList.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(imageVector = item.icon, contentDescription = null,tint = Color(0xFFFF3509)) },
                        label = { Text(text = item.label) },
                        selected = (item == selectedItem.value),
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            selectedItem.value = item
                            when (item.label) {
                                "Admin" -> navController.navigate(Screen.Home.route) {
                                    // Clear back stack
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination
                                    launchSingleTop = true
                                    // Restore state when revisiting a screen
                                    restoreState = true
                                }
                                "Order History" -> navController.navigate(Screen.Home.route) {
                                    // Clear back stack
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination
                                    launchSingleTop = true
                                    // Restore state when revisiting a screen
                                    restoreState = true
                                }
                                "Logout" -> navController.navigate(Screen.Login.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {

        Column(modifier = Modifier) {
            CenterAlignedTopAppBar(
                title = { Text(text = "Table Tap", color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFFF3509)),
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch { drawerState.open() }
                    }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu",tint = Color.White)
                    }
                },
            )
        }

    }
}


data class NavigationDrawerData(val label: String, val icon: ImageVector)

private fun prepareNavigationDrawerItems(): List<NavigationDrawerData> {
    val drawerItemsList = arrayListOf<NavigationDrawerData>()

    // add items
    drawerItemsList.add(NavigationDrawerData(label = "Admin", icon = Icons.Filled.Person))
    drawerItemsList.add(NavigationDrawerData(label = "Order History", icon = Icons.Filled.History))
    drawerItemsList.add(NavigationDrawerData(label = "Logout", icon = Icons.Filled.Logout))

    return drawerItemsList
}
@Composable
fun MyScreen() {
    val navController = rememberNavController()
    val contextForToast = LocalContext.current.applicationContext
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    sharedPreferencesManager = SharedPreferencesManager(context = contextForToast)
    val isAdmin = sharedPreferencesManager.role ?: "table"
    Scaffold(
        topBar = {

            MyUI(navController = navController)
        },
        bottomBar = {
            if (isAdmin === "admin") {
                MyAdminBottomBar(navController)
            } else {
                MyUserBottomBar(navController)
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavGraph(navController = navController)
        }
    }
}
