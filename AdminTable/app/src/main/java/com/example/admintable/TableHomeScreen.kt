package com.example.admintable

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("RestrictedApi")
@Composable
fun TableHomeScreen(navController: NavHostController) {
    val createClient = TableAPI.create()
    var tableItemsList = remember { mutableStateListOf<Table>() }
    val contextForToast = LocalContext.current.applicationContext
    var changeStatusDialog by remember { mutableStateOf(false) }
    var textFieldID by remember { mutableStateOf("") }
    var textFieldName by remember { mutableStateOf("") }

    // Check Lifecycle State
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {

        when (lifecycleState) {
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {}
            Lifecycle.State.RESUMED -> {
                showAll(tableItemsList, contextForToast)
            }
        }
    }
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "All Table",
                fontSize = 25.sp
            )
        }
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 110.dp),
            verticalArrangement = Arrangement.spacedBy(space = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 5.dp),
            contentPadding = PaddingValues(all = 8.dp)
        ) {
            var itemClick = Table("", "", "","", 0, "")
            itemsIndexed(
                items = tableItemsList,
                itemContent = { _, item ->
                    val iconTint = if (item.status == "ว่าง") Color(0xFF019E07) else Color.Red
                    val statusText = if (item.status == "ว่าง") "ว่าง" else "กำลังใช้งาน"
                    val statusTextColor = if (item.status == "ว่าง") Color(0xFF019E07) else Color.Red

                    val onItemClick: (Table) -> Unit = { table ->
                        if (table.status == "ว่าง") {
                            changeStatusDialog = true
                        } else {
                            // นำผู้ใช้ไปยังหน้าบิล
                            navController.navigate("bill_screen/${table.table_id}")
                        }
                    }
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .width(160.dp)
                            .height(120.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        ),
                        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                        onClick = {
                            changeStatusDialog = true
                            textFieldID = item.table_id
                            textFieldName = item.username
                            Toast.makeText(
                                contextForToast, "Click on ${item.table_number}.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                            .width(130.dp)
                            .height(180.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.table),
                                tint = iconTint,
                                modifier = Modifier //.align(Alignment.TopCenter)
                                    .size(50.dp),
                                contentDescription = "table"

                            )
                            Text(
                                text = "Table No. ${item.table_number}",
                                color = Color.Black,
                                fontSize = 16.sp
                            )
                            Text(
                                text = statusText,
                                color = statusTextColor,
                                fontSize = 16.sp
                            )
                        }


                    if (changeStatusDialog && item.status == "ว่าง") {
                        AlertDialog(
                            onDismissRequest = { changeStatusDialog = false },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        changeStatusDialog = false
                                        createClient.updateStatusTable(textFieldID, "ไม่ว่าง").enqueue(object : Callback<Table>{
                                            override fun onResponse(call: Call<Table>, response: Response<Table>) {
                                                if(response.isSuccessful){
                                                    Toast.makeText(contextForToast,"Successfully Change the status",Toast.LENGTH_LONG).show()
                                                }else{
                                                    Toast.makeText(contextForToast,"Change the status Failure ${response.code()}",Toast.LENGTH_LONG).show()
                                                }
                                            }

                                            override fun onFailure(call: Call<Table>, t: Throwable) {
                                                Toast.makeText(contextForToast,"Error onFailure"+t.message,Toast.LENGTH_LONG).show()
                                            }

                                        })

                                        navController.popBackStack()
                                        navController.navigate(Screen.TableHome.route)
                                    }
                                ) {
                                    Text("Yes")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        changeStatusDialog = false
                                        Toast.makeText(contextForToast,"Click on No",Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Text("No")
                                }
                            },
                            title = { Text("Warning") },
                            text = { Text("Do you want to change the status Table No. ${textFieldName}?") },
                        )
                    }else{
//                        navController.navigate("bill_screen/${table.tb_id}")
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            "data",
                            Table(
                                item.table_id,
                                item.username,
                                item.table_number,
                                item.status,
                                item.seat_count,
                                item.password
                            )
                        )
//                        navController.navigate(Screen.TableHome.route)
                    }


                    }

        })   }   } }
//                    Box(
//                        modifier = Modifier
//                            //    .shadow(elevation = 10.dp , spotColor = Color.LightGray, shape = RoundedCornerShape(8.dp))
//                            .padding(10.dp) // ความห่างข้างนอก box
//                            .width(130.dp)
//                            .height(140.dp)
//                            .border(0.5.dp, Color.LightGray, shape = RoundedCornerShape(8.dp))
//                            .clip(shape = RoundedCornerShape(8.dp))
//                            .background(Color.White)
//                            .padding(8.dp)
//                            .clickable {
//                                // handle click action here
//                                if (item.status == "ว่าง") {
//                                    showChangeStatusDialog(item)
//                                } else {
//                                    // navigate to bill screen
//                                    navController.navigate("bill_screen/${item.tb_id}")
//                                }
//                            }, // ตวามห่างระหว่าง content
//                        contentAlignment = Alignment.Center,
//
//
//                    ){
//
//                        Icon(painter = painterResource(R.drawable.table),
//                            tint = iconTint,
//                            modifier = Modifier.align(Alignment.TopCenter)
//                                .size(50.dp),
//                            contentDescription = "dest"
//
//                        )
//                        Text(
//                            textAlign = TextAlign.Center,
//                            text = "Table No. ${item.tb_name}",
//                            color = Color.Black,
//                            fontSize = 16.sp,
//                            modifier = Modifier.align(Alignment.Center)
//                        )
//                        Text(
//                            text = statusText,
//                            color = statusTextColor,
//                            fontSize = 16.sp,
//                            modifier = Modifier.align(Alignment.BottomCenter)
//                        )
//                    }




//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Card(
//                            modifier = Modifier
//                                .padding(horizontal = 8.dp, vertical = 8.dp)
////                                .fillMaxWidth()'
//                                .width(100.dp)
//                                .height(100.dp),
//                            colors = CardDefaults.cardColors(
//                                containerColor = Color.White,
//                            ),
//                            elevation = CardDefaults.cardElevation(
//                                defaultElevation = 2.dp
//                            ),
//                            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
//                            onClick = {
//                                Toast.makeText(
//                                    contextForToast, "Click on ${item.tb_name}.",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            },
//                        ) {
//                            Column(
//                                verticalArrangement = Arrangement.Center,
//                                horizontalAlignment = Alignment.CenterHorizontally
//                            ){
//                                Icon(
//                                        imageVector = Icons.Default.AccountBox,
//                                        contentDescription = null,
//                                        tint = iconTint
//                                )
//
//                                Text(
//                                    textAlign = TextAlign.Center,
//                                    text = "Table No. ${item.tb_name}",
//                                    color = Color.Black,
//                                    fontSize = 16.sp
//                                )
//                                Text(
//                                    text = statusText,
//                                    color = statusTextColor,
//                                    fontSize = 16.sp
//                                )
//                            }
//                        }
//                    }
//                }
//            )
//        }
//    }
//    ChangeStatusDialog(
//        showDialog = changeStatusDialog,
//        onConfirm = {
//            changeStatusDialog = false
//        },
//        onDismiss = { changeStatusDialog = false }
//    )
//}

fun showAll(tableItemsList: MutableList<Table>, context: Context) {
    val createClient = TableAPI.create()
    createClient.retrieveTable()
        .enqueue(object : Callback<List<Table>> {
            override fun onResponse(
                call: Call<List<Table>>,
                response: Response<List<Table>>
            ) {
                tableItemsList.clear()
                response.body()?.forEach {
                    tableItemsList.add(
                        Table(
                            it.table_id,
                            it.username,
                            it.table_number,
                            it.status,
                            it.seat_count,
                            it.password
                        )
                    )
                }
            }

            override fun onFailure(call: Call<List<Table>>, t: Throwable) {
                Toast.makeText(context, "Error onFailure " + t.message, Toast.LENGTH_LONG)
                    .show()
            }
        })

}
