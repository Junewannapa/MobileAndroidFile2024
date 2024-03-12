package com.example.tabletap

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
fun HomeScreen(navController: NavHostController) {
    val createClient = TableAPI.create()
    var tableItemsList = remember { mutableStateListOf<Table>() }
    val contextForToast = LocalContext.current.applicationContext
    var changeStatusDialog by remember { mutableStateOf(false) }
    var textFieldID by remember { mutableStateOf("") }
    var textFieldName by remember { mutableStateOf("") }
    var textFieldNumber by remember { mutableStateOf("") }

    lateinit var shar : SharedPreferencesManager
    shar= SharedPreferencesManager(contextForToast)
    // Check Lifecycle State
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {

        when (lifecycleState) {
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {
                if(!shar.isLoggedIn){
                    starLogin(contextForToast)
                }
            }
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
                            changeStatusDialog = false
                            Toast.makeText(contextForToast,"Click on No",Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.BillAdmin.route.replace("{tableId}", "${table.table_id}"))
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
                            if (item.status == "ว่าง") {
                                changeStatusDialog = true
                                textFieldID = item.table_id
                                textFieldName = item.username
                                textFieldNumber = item.table_number
                                Toast.makeText(
                                    contextForToast, "Click on ${item.table_number}.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                changeStatusDialog = false
                                navController.navigate(Screen.BillAdmin.route.replace("{tableId}", "${item.table_id}"))
                            }
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
                                        navController.navigate(Screen.Home.route)
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
                            text = { Text("Do you want to change the status Table No. ${textFieldNumber}?") },
                        )
                    }else{
                        //navController.navigate(Screen.BillAdmin.route.replace("{tableId}", "${item.table_id}"))
//                        navController.currentBackStackEntry?.savedStateHandle?.set(
//                            "data",
//                            Table(
//                                item.table_id,
//                                item.username,
//                                item.table_number,
//                                item.status,
//                                item.seat_count,
//                                item.password
//                            )
//                        )

                    }


                    }

        })   }   } }

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
