package com.example.pjtable

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableEditScreen(navController: NavHostController) {
    val data = navController.previousBackStackEntry?.savedStateHandle?.get<Table>("data") ?:
    Table(td_id = "", td_name = "", status = "", seat_count = 0, tb_pw = "")

    var textFieldID by remember { mutableStateOf(TextFieldValue(data.td_id)) }
    var textFieldName by remember { mutableStateOf(TextFieldValue(data.td_name)) }
  //  var textFieldStatus by remember { mutableStateOf(TextFieldValue(data.status)) }
    var textFieldSeat_count by remember { mutableStateOf(TextFieldValue(data.seat_count.toString())) }
    var textFieldPassword by remember { mutableStateOf(TextFieldValue(data.tb_pw)) }

    val contextForToast = LocalContext.current
    var statusValue by remember { mutableStateOf(data.status) }
    val createClient = TableAPI.create()
    var deleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Edit a student",
            fontSize = 25.sp
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldID,
            onValueChange = { textFieldID = it },
            label = { Text("Table ID")},
            enabled = false,
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldName,
            onValueChange = { textFieldName = it },
            label = { Text("กรอกชื่อ/เลขโต๊ะ")},

        )
        statusValue = EditRadioGroupUsage(statusValue)
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldSeat_count,
            onValueChange = { textFieldSeat_count = it },
            label = { Text("กรอกจำนวนที่นั่ง")},
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldPassword,
            onValueChange = { textFieldPassword = it },
            label = { Text("กรอกรหัสผ่านของโต๊ะ")},
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Button(
                modifier = Modifier
                    .width(100.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color.Red,
                ),
                onClick = {
                    deleteDialog = true

                }
            ) {
                Text(text = "Delete")
            }
            if (deleteDialog) {
                AlertDialog(
                    onDismissRequest = { deleteDialog = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                deleteDialog = false
                                createClient.deleteTable(data.td_id).enqueue(object : Callback<Table>{
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
                    text = { Text("Do you want to delete a student: ${data.td_id} ?") },

                    )
            }
            Spacer(modifier = Modifier.width(10.dp))

            Button(
                modifier = Modifier.width(100.dp),
                onClick = {
                    createClient.updateTable(
                        textFieldID.text,
                        textFieldName.text,
                        statusValue,
                        textFieldSeat_count.text.toInt(),
                        textFieldPassword.text
                    ).enqueue(object : Callback<Table> {
                        override fun onResponse(call: Call<Table>, response: Response<Table>) {
                            if (response.isSuccessful) {
                                Toast.makeText(contextForToast, "Successfully Updated "+textFieldName, Toast.LENGTH_LONG).show()
                                if (navController.currentBackStackEntry?.destination?.route != Screen.TableHome.route) {
                                    navController.popBackStack()
                                } else {
                                    navController.navigate(Screen.TableHome.route)
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
                        navController.navigate(Screen.TableHome.route)
                    }
                }) {
                Text(text = "Cancel")
            }
        }
    }
}

@Composable
fun EditRadioGroupUsage(s: String): String {
    val status = listOf("ว่าง","ไม่ว่าง")
    var (selected, setSelected) = remember { mutableStateOf(s) }
    Text(
        text = "Table Status :",
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp),
    )
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp)) {
        EditRadioGroup(
            mItems = status,
            selected, setSelected
        )
    }
    return selected
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
                    onClick = { setSelected(item) },
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