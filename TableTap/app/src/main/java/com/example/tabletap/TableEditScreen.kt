package com.example.tabletap

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@SuppressLint("RestrictedApi")
@Composable
fun TableEditScreen(navController: NavHostController) {
    val data = navController.previousBackStackEntry?.savedStateHandle?.get<Table>("data") ?:
    Table(table_id="",username = "", table_number ="", status = "", seat_count = 0, password = "")

    var textFieldID by remember { mutableStateOf((data.table_id)) }
    var textFieldName by remember { mutableStateOf(TextFieldValue(data.username)) }
    var textFieldNumber by remember { mutableStateOf(TextFieldValue(data.table_number)) }
    var textFieldPW by remember { mutableStateOf(TextFieldValue(data.password)) }
    var textFieldSeatCount by remember { mutableStateOf(TextFieldValue(data.seat_count.toString())) }


    val contextForToast = LocalContext.current
    var statusValue by remember { mutableStateOf(data.status) }
    val createClient = TableAPI.create()
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Edit Table",
            fontSize = 25.sp
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldName,
            onValueChange = { textFieldName = it },
            label = { Text("Table Name")},

        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldNumber,
            onValueChange = { textFieldNumber=it },
            label = { Text("Table Number") }
        )
        EditRadioGroup(
            mItems = listOf("ว่าง", "ไม่ว่าง"),
            selected = statusValue,
            setSelected = { statusValue = it }
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldSeatCount,
            onValueChange = { textFieldSeatCount = it },
            label = { Text("Seat Count")},
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier.width(100.dp),
                onClick = {
                    createClient.updateTable(
                        textFieldID,
                        textFieldName.text,
                        textFieldNumber.text,
                        statusValue,
                        textFieldSeatCount.text.toInt(),
                        textFieldPW.text
                    ).enqueue(object : Callback<Table> {
                        override fun onResponse(call: Call<Table>, response: Response<Table>) {
                            if (response.isSuccessful) {
                                Toast.makeText(contextForToast, "Successfully Updated "+textFieldName.text, Toast.LENGTH_LONG).show()
                                if (navController.currentBackStackEntry?.destination?.route != Screen.Home.route) {
                                    navController.popBackStack()
                                } else {
                                    navController.navigate(Screen.Home.route)
                                }
                            } else {
                                Toast.makeText(contextForToast, "Update Failure", Toast.LENGTH_LONG).show()
                            }
                        }
                        override fun onFailure(call: Call<Table>, t: Throwable) {
                            Toast.makeText(contextForToast, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
                        }
                    })
                }
            ) {
                Text(text = "Update")
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                modifier = Modifier
                    .width(100.dp),
                onClick = {
                    if (navController.currentBackStack.value.size > 2) {
                        navController.popBackStack()
                    } else {
                        navController.navigate(Screen.Table.route)
                    }
                }) {
                Text(text = "Cancel")
            }
        }
    }
}

@Composable
fun EditRadioGroup(
    mItems: List<String>,
    selected: String,
    setSelected: (selected: String) -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        mItems.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selected == item,
                    onClick = {
                        setSelected(item)
                    },
                    enabled = true,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Green
                    )
                )
                Text(text = item, modifier = Modifier.padding(start = 5.dp))
            }
        }
    }
}