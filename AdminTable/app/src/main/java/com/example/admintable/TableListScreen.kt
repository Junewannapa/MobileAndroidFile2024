package com.example.admintable

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("RestrictedApi")
@Composable
fun TableListScreen(navController: NavHostController) {
    val createClient = TableAPI.create()
    var tableItemsList = remember { mutableStateListOf<Table>() }
    val contextForToast = LocalContext.current.applicationContext

    var textFieldID by remember { mutableStateOf("") }
    var textFieldName by remember { mutableStateOf("") }

    // Check Lifecycle State
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    var deleteDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

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
                value = textFieldName,
                onValueChange = { textFieldName = it },
                label = { Text(text = "Table Name") }
            )
            Button(onClick = {
                if (textFieldName.trim().isEmpty()) {
                    showAllData(tableItemsList, contextForToast)
                } else {
                    tableItemsList.clear()
                    createClient.retrieveTableID(textFieldName)
                        .enqueue(object : Callback<Table> {
                            override fun onResponse(
                                call: Call<Table>,
                                response: Response<Table>
                            ) {
                                if (response.isSuccessful) {
                                    tableItemsList.add(
                                        Table(
                                            response.body()!!.table_id,
                                            response.body()!!.username,
                                            response.body()!!.table_number,
                                            response.body()!!.status,
                                            response.body()!!.seat_count,
                                            response.body()!!.password
                                        )
                                    )
                                    Toast.makeText(
                                        contextForToast,
                                        "Table ID Found", Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        contextForToast,
                                        "Table Not ID Found", Toast.LENGTH_LONG
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
                Text(text = "Add Table")
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            var itemClick = Table( "",  "",  "", "", 0,"")
            itemsIndexed(
                items = tableItemsList,
                itemContent = { _, item ->

                    Card(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .fillMaxWidth()
                            .height(120.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        ),
                        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                        onClick = {
                            Toast.makeText(
                                contextForToast, "Click on ${item.table_number}.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(Dp(value = 100f))
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(15.dp))
                            Text(
                                text = "Table No: ${item.table_number}\n" +
                                        "Seat: ${item.seat_count} people\n" +
                                        "Status: ${item.status}",
                                Modifier.weight(0.85f),
                                fontSize = 18.sp
                            )
                            IconButton(onClick = {
                                itemClick = item
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
                                navController.navigate(Screen.TableEdit.route)
                            }
                            ) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = null,
                                    tint = Color(0xFFFDB500)
                                )
                            }
                              
                            Spacer(modifier = Modifier.width(5.dp))
                            IconButton(onClick = {
                                deleteDialog = true
                                textFieldID = item.table_id
                                textFieldName = item.username
                                Toast.makeText(contextForToast,"del",Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = null,
                                    tint = Color.Red
                                )

                            }
                            if (deleteDialog) {
                                AlertDialog(
                                    onDismissRequest = { deleteDialog = false },
                                    confirmButton = {
                                        TextButton(
                                            onClick = {
                                                deleteDialog = false
                                                createClient.deleteTable(textFieldID
                                                ).enqueue(object : Callback<Table>{
                                                    override fun onResponse(call: Call<Table>,
                                                                            response: Response<Table>
                                                    ) {
                                                        if(response.isSuccessful){
                                                            Toast.makeText(contextForToast,"Successfully Deleted",Toast.LENGTH_LONG).show()
                                                        }else{
                                                            Toast.makeText(contextForToast,"Delete Failure ${response.code()}",Toast.LENGTH_LONG).show()
                                                        }
                                                    }

                                                    override fun onFailure(call: Call<Table>, t: Throwable) {
                                                        Toast.makeText(contextForToast,"Error onFailure"+t.message,Toast.LENGTH_LONG).show()
                                                    }

                                                })
                                                navController.popBackStack()
                                                navController.navigate(Screen.TableList.route)
                                            }
                                        ) {
                                            Text("Yes")
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(
                                            onClick = {
                                                deleteDialog = false
                                                Toast.makeText(contextForToast,"Click on No",Toast.LENGTH_SHORT).show()
                                            }
                                        ) {
                                            Text("No")
                                        }
                                    },
                                    title = { Text("Warning") },
                                    text = { Text("Do you want to delete a student: ${textFieldName} ?") },

                                    )
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