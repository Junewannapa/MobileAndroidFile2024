package com.example.tabletap


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("RestrictedApi")
@Composable
fun BillAdminScreen(navController: NavHostController, tableId: String?) {
    val data = navController.previousBackStackEntry?.savedStateHandle?.get<Table>("data") ?:
    Table(table_id="",username = "", table_number ="", status = "", seat_count = 0, password = "")

    var textFieldID by remember { mutableStateOf((data.table_id)) }
    val contextForToast = LocalContext.current.applicationContext
    var changeStatusDialog by remember { mutableStateOf(false) }

    val createClient = TableAPI.create()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var itemClick = Table("", "", "","", 0, "")
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Bill Table No:",
            fontSize = 25.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        )
        {
            Button(modifier = Modifier
                .width(130.dp),
                onClick = {
                    changeStatusDialog = true
                }) {
                Text("Payment")
            }


            if (changeStatusDialog) {
                AlertDialog(
                    onDismissRequest = { changeStatusDialog = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                changeStatusDialog = false
                                if (tableId != null) {
                                    createClient.updateStatusTable(tableId, "ว่าง").enqueue(object : Callback<Table>{
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
                                }

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
                            }
                        ) {
                            Text("No")
                        }
                    },
                    title = { Text("Warning") },
                    text = { Text("Do you want to change the status of Table No. $tableId to 'ว่าง'?") },
                )
            }
        }
    }
}
