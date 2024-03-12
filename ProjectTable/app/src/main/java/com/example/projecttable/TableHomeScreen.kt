package com.example.projecttable

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableHomeScreen(navController: NavHostController) {
    val createClient = TableAPI.create()
    var tableItemsList = remember { mutableStateListOf<Table>() }
    val contextForToast = LocalContext.current.applicationContext
    var textFieldID by remember { mutableStateOf("") }

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
                showAllData(tableItemsList, contextForToast)
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
                text = "Search:",
                fontSize = 20.sp
            )
            OutlinedTextField(
                modifier = Modifier
                    .width(230.dp)
                    .padding(10.dp),
                value = textFieldID,
                onValueChange = { textFieldID = it },
                label = { Text(text = "Table ID") }
            )
            Button(onClick = {
                if (textFieldID.trim().isEmpty()) {
                    showAllData(tableItemsList, contextForToast)
                } else {
                    tableItemsList.clear()
                    createClient.retrieveTableID(textFieldID)
                        .enqueue(object : Callback<Table> {
                            override fun onResponse(
                                call: Call<Table>,
                                response: Response<Table>
                            ) {
                                if (response.isSuccessful) {
                                    tableItemsList.add(
                                        Table(
                                            response.body()!!.td_id,
                                            response.body()!!.td_name,
                                            response.body()!!.status,
                                            response.body()!!.seat_count,
                                            response.body()!!.tb_pw
                                        )
                                    )
                                    Toast.makeText(
                                        contextForToast,
                                        "Student ID Found", Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        contextForToast,
                                        "Student Not ID Found", Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<Table>, t: Throwable) {
                                Toast.makeText(
                                    contextForToast, "Error onFailure " + t.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
                }
            })
            {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
        }

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
                    text = "Table Lists: ${tableItemsList.size} ",
                    fontSize = 25.sp
                )
            }
            Button(onClick = {
                if (navController.currentBackStack.value.size >= 2) {
                    navController.popBackStack()
                } else {
                    navController.navigate(Screen.TableInsert.route)
                }
            }) {
                Text(text = "Add Student")
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            var itemClick = Table( "",  "",  "",  0,"")
            itemsIndexed(
                items = tableItemsList,
                itemContent = { index, item ->
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .fillMaxWidth()
                            .height(130.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        ),
                        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                        onClick = {
                            Toast.makeText(
                                contextForToast, "Click on ${item.td_name}.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(Dp(value = 130f))
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ชื่อ/หมายเลขโต๊ะ: ${item.td_name}\n" +
                                        "สถานะ: ${item.status}\n" +
                                        "สำหรับ: ${item.seat_count}",
                                Modifier.weight(0.85f)
                            )
                            TextButton(onClick = {
                                val itemClick = item
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "data",
                                    Table(
                                        item.td_id,
                                        item.td_name,
                                        item.status,
                                        item.seat_count,
                                        item.tb_pw
                                    )
                                )
                                navController.navigate(Screen.TableEdit.route)
                            }
                            )
                            {
                                Text(text = "Edit/Delete")
                            }
                        }
                    }
                })   }   }   }
fun showAllData(tableItemsList: MutableList<Table>, context: Context) {
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
                            it.td_id,
                            it.td_name,
                            it.status,
                            it.seat_count,
                            it.tb_pw
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