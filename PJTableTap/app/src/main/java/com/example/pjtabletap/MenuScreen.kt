package com.example.pjtabletap


import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.DeleteForever
import androidx.compose.material.icons.sharp.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.State.*
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(navController: NavController) {
    val createClient = MenuAPI.create()
    val menuList = remember { mutableStateListOf<MenuClass>() }
    val contextForToast = LocalContext.current.applicationContext
    var deleteDialog by remember { mutableStateOf(false) }
    var softDeleteDialog by remember { mutableStateOf(false) }
    var food_id by remember { mutableStateOf(0) }
    var food_name by remember { mutableStateOf("") }

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {}
            Lifecycle.State.RESUMED -> {
                showAllMenu(menuList, contextForToast)
            }
        }
    }
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Column(modifier = Modifier.weight(0.85f))
            {
                Text(
                    text = "Menu List:",
                    fontSize = 25.sp
                )
            }
            Column()
            {
                Button(modifier = Modifier.width(180.dp),
                    onClick = {
                        if (navController.currentBackStack.value.size >= 2) {
                            navController.popBackStack()
                        }
                        navController.navigate(Screen.InsertMenu.route)
                    }) {
                    Text("Add Menu")
                }
                Button(modifier = Modifier.width(180.dp), onClick = {
                    if (navController.currentBackStack.value.size >= 2) {
                        navController.popBackStack()
                    }
                    navController.navigate(Screen.InsertMenuType.route)
                }) {
                    Text("Add Menu Type")
                }
            }
        }
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 190.dp),
            verticalArrangement = Arrangement.spacedBy(space = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 5.dp),
            contentPadding = PaddingValues(all = 10.dp)
        ) {
            var itemClick = MenuClass(0, "", 0, "",0,0)
            itemsIndexed(
                items = menuList,
            ) { index, item ->
                Card(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 5.dp, vertical = 5.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    ),
                    shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                    onClick = {
                        Toast.makeText(
                            contextForToast, "Click on ${item.food_name}.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ) {
                    Column(
                        Modifier
                            .width(190.dp)
                            .wrapContentHeight()
                            .padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(item.food_img),
                            contentDescription = item.food_name,
                            modifier = Modifier
                                .height(150.dp)
                                .width(150.dp)
                        )
                        Text(
                            text = "${item.food_name}\n" +
                                    "${item.price}",
                            textAlign = TextAlign.Center
                        )
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(
                                onClick = {
                                    itemClick = item
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "data",
                                        MenuClass(
                                            item.food_id,
                                            item.food_name,
                                            item.price,
                                            item.food_img,
                                            item.food_type_id,
                                            item.food_quantity
                                        )
                                    )
                                    navController.navigate(Screen.EditMenu.route)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Sharp.Edit,
                                    contentDescription = "Edit",
                                    tint = Color(76, 175, 80, 255)
                                )
                            }
                            IconButton(
                                onClick = {
                                    deleteDialog = true
                                    food_id = item.food_id
                                    food_name = item.food_name
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Sharp.DeleteForever,
                                    contentDescription = "Delete",
                                    tint = Color.Red
                                )
                            }
                        }
                    }

                }

            }
        }
    }
    if (deleteDialog) {
        AlertDialog(
            onDismissRequest = { deleteDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteDialog = false
                        createClient.deleteMenu(
                            food_id
                        ).enqueue(object : Callback<MenuClass> {
                            override fun onResponse(
                                cal: Call<MenuClass>,
                                response: Response<MenuClass>
                            ) {
                                if (response.isSuccessful) {
                                    Toast.makeText(
                                        contextForToast, "Successfully Deleted",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        contextForToast, "Delete Failure",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<MenuClass>, t: Throwable) {
                                Toast.makeText(
                                    contextForToast, " Error onFailure" + t.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
                        showAllMenu(menuList, contextForToast)
                    }
                ) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        deleteDialog = false
                        Toast.makeText(
                            contextForToast,
                            "Click on No", Toast.LENGTH_SHORT
                        ).show()
                    }
                ) {
                    Text(text = "No")
                }
            },
            title = { Text(text = "Warning") },
            text = { Text(text = "Do you want to delete a menu: $food_name?") }
        )
    }
}


fun showAllMenu(menuList:MutableList<MenuClass>,context: Context){
    val createClient = MenuAPI.create()
    menuList.clear()
    createClient.retrieveMenu()
        .enqueue(object : Callback<List<MenuClass>>{
            override fun onResponse(
                call: Call<List<MenuClass>>,
                response: Response<List<MenuClass>>
            ){
                response.body()?.forEach{
                    menuList.add(MenuClass(it.food_id,it.food_name,it.price,it.food_img,it.food_type_id,it.food_quantity)
                    )
                }
            }
            override fun onFailure(call: Call<List<MenuClass>>, t: Throwable){
                Toast.makeText(
                    context, "Error onFailure" +t.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        })

}




