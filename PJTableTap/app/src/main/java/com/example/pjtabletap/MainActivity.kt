package com.example.pjtabletap

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pjtabletap.ui.theme.PJTableTapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PJTableTapTheme {
                // A surface container using the 'background' color from the theme
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
    PJTableTapTheme {
        Greeting("Android")
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun MyBottomBar(navController: NavHostController) {
    val navigationItems = listOf(

        Screen.Menu
    )
    var selectedScreen by remember {
        mutableIntStateOf(0)
    }
    val resources = LocalContext.current.resources
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
                            "com.example.pjtabletap"
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
    }
}

@Composable
fun MyScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { MyBottomBar(navController) },
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