package com.example.ass08mysql_queryinsert

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
fun EditScreen(navController: NavHostController) {
    val data = navController.previousBackStackEntry?.savedStateHandle?.get<Employee>("data") ?:
    Employee(emp_id=0,emp_name = "", emp_gender = "", emp_email = "", emp_salary = 0)

    var ID by remember { mutableStateOf((data.emp_id)) }
    var textFieldName by remember { mutableStateOf(TextFieldValue(data.emp_name)) }
    var textFieldEmail by remember { mutableStateOf(TextFieldValue(data.emp_email)) }
    var textFieldSalary by remember { mutableStateOf(TextFieldValue(data.emp_salary.toString())) }


    val contextForToast = LocalContext.current
    var genderValue by remember { mutableStateOf(data.emp_gender) }
    val createClient = EmployeeAPI.create()
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Edit an employee",
            fontSize = 25.sp
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldName,
            onValueChange = { textFieldName = it },
            label = { Text("Employee")},
        )
        EditRadioGroup(
            mItems = listOf("Male", "Female","Other"),
            selected = genderValue,
            setSelected = { genderValue = it }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldEmail,
            onValueChange = { textFieldEmail = it },
            label = { Text("Email")},
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldSalary,
            onValueChange = { textFieldSalary = it },
            label = { Text("Salary")},
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
                    showDeleteDialog = true
                }
            ) {
                Text(text = "Delete")
            }

            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showDeleteDialog = false
                    },
                    title = { Text("Warning") },
                    text = { Text("Do you want to delete this employee:${textFieldName.text}?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                createClient.deleteEmployee(ID.toString()).enqueue(object : Callback<Employee> {
                                    override fun onResponse(call: Call<Employee>, response: Response<Employee>) {
                                        if (response.isSuccessful) {
                                            Toast.makeText(contextForToast, "Successfully Deleted", Toast.LENGTH_LONG).show()
                                            if (navController.currentBackStack.value.size > 2) {
                                                navController.popBackStack()
                                            } else {
                                                navController.navigate(Screen.Home.route)
                                            }
                                        } else {
                                            Toast.makeText(contextForToast, "Delete Failure", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                    override fun onFailure(call: Call<Employee>, t: Throwable) {
                                        Toast.makeText(contextForToast, "Error onFailure" + t.message, Toast.LENGTH_LONG).show()
                                    }
                                })
                                // Dismiss the dialog after clicking Yes
                                showDeleteDialog = false
                            },
                        ) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                // Dismiss the dialog when clicking No
                                showDeleteDialog = false
                            },
                        ) {
                            Text("No")
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                modifier = Modifier.width(100.dp),
                onClick = {
                    createClient.updateEmployee(
                        ID.toString(),
                        textFieldName.text,
                        genderValue,
                        textFieldEmail.text,
                        textFieldSalary.text.toInt()
                    ).enqueue(object : Callback<Employee> {
                        override fun onResponse(call: Call<Employee>, response: Response<Employee>) {
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
                        override fun onFailure(call: Call<Employee>, t: Throwable) {
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
                        navController.navigate(Screen.Home.route)
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