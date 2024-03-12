package com.example.lab8mysql_queryinsert

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
    val data = navController.previousBackStackEntry?.savedStateHandle?.get<Student>("data") ?:
    Student(std_id = "", std_name = "", std_gender = "", std_age = 0)

    var textFieldID by remember { mutableStateOf(TextFieldValue(data.std_id)) }
    var textFieldName by remember { mutableStateOf(TextFieldValue(data.std_name)) }
    var textFieldAge by remember { mutableStateOf(TextFieldValue(data.std_age.toString())) }

    val contextForToast = LocalContext.current
    var genderValue by remember { mutableStateOf(data.std_gender) }
    val createClient = StudentAPI.create()
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
            label = { Text("Student ID")},

        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldName,
            onValueChange = { textFieldName = it },
            label = { Text("Student Name")},
        )
        genderValue = EditRadioGroupUsage(genderValue)
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldAge,
            onValueChange = { textFieldAge = it },
            label = { Text("Student Age")},
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
                                createClient.deleteStudent(data.std_id).enqueue(object : Callback<Student>{
                                    override fun onResponse(call: Call<Student>,
                                        response: Response<Student>
                                    ) {
                                        if(response.isSuccessful){
                                            Toast.makeText(contextForToast,"Successfully Deleted",Toast.LENGTH_LONG).show()
                                        }else{
                                            Toast.makeText(contextForToast,"Delete Failure ${response.code()}",Toast.LENGTH_LONG).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<Student>, t: Throwable) {
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
                    text = { Text("Do you want to delete a student: ${data.std_id} ?") },

                    )
            }
            Spacer(modifier = Modifier.width(10.dp))

            Button(
                modifier = Modifier.width(100.dp),
                onClick = {
                    createClient.updateStudent(
                        textFieldID.text,
                        textFieldName.text,
                        genderValue,
                        textFieldAge.text.toInt()
                    ).enqueue(object : Callback<Student> {
                        override fun onResponse(call: Call<Student>, response: Response<Student>) {
                            if (response.isSuccessful) {
                                Toast.makeText(contextForToast, "Successfully Updated "+textFieldID, Toast.LENGTH_LONG).show()
                                if (navController.currentBackStackEntry?.destination?.route != Screen.Home.route) {
                                    navController.popBackStack()
                                } else {
                                    navController.navigate(Screen.Home.route)
                                }
                            } else {
                                Toast.makeText(contextForToast, "Update Failure", Toast.LENGTH_LONG).show()
                            }
                        }
                        override fun onFailure(call: Call<Student>, t: Throwable) {
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
fun EditRadioGroupUsage(s: String): String {
    val kinds = listOf("Male", "Female", "Other")
    var (selected, setSelected) = remember { mutableStateOf(s) }
    Text(
        text = "Student Gender: ",
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp),
    )
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp)) {
        EditRadioGroup(
            mItems = kinds,
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