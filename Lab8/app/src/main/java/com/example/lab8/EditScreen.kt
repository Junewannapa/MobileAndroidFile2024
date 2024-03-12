package com.example.lab8

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(navController: NavController) {
    val data = navController.previousBackStackEntry?.savedStateHandle?.get<Student>("data") ?:
    Student("", "", "", 0)

    var textFieldID by remember { mutableStateOf(data.std_id) }
    var textFieldName by remember { mutableStateOf(data.std_name) }
    var textFieldAge by remember { mutableIntStateOf(data.std_age) }
    val contextForToast = LocalContext.current
    var genderValue by remember { mutableStateOf(data.std_gender) }
    val createClient = StudentAPI.create()

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
            label = { Text("Student ID") },
            enabled = false,
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldName,
            onValueChange = { textFieldName = it },
            label = { Text("Student Name") }
        )

//        OutlinedTextField(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            value = textFieldAge,
//            onValueChange = { textFieldAge = it },
//            label = { Text("Student Age") }
//        )


        genderValue = EditRadioGroupUsage(genderValue)

        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier
                    .width(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                onClick = {

                }
            ) {
                Text("Delete")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                modifier = Modifier
                    .width(100.dp),
                onClick = {
                    createClient.updateStudent(
                        textFieldID, " ", genderValue, 0
                    ).enqueue(object : Callback<Student> {
                        override fun onResponse(call: Call<Student>, response: Response<Student>) {
                            if (response.isSuccessful) {
                                Toast.makeText(contextForToast, "Successfully Update",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(contextForToast, "Update Failure",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<Student>, t: Throwable) {
                            Toast.makeText(
                                contextForToast, "Error onFailure" + t.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                    if (navController.currentBackStack.value.size >= 2) {
                        navController.popBackStack()
                    }
                    navController.navigate(Screen.Home.route)
                }
            ) {
                Text("Update")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                modifier = Modifier
                    .width(100.dp),
                onClick = {
                    if (navController.currentBackStack.value.size >= 2) {
                        navController.popBackStack()
                    }
                    navController.navigate(Screen.Home.route)
                }
            ) {
                Text("Cancel")
            }
        }
    }
}

@Composable
fun EditRadioGroupUsage(s: String): String {
    val kinds = listOf("Male", "Female", "Other")
    var (selectedGender, setSelected) = remember { mutableStateOf(s) }

    Text(
        text = "Student Gender : ",
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp),
    )

    Row(modifier = Modifier.fillMaxWidth().padding(start = 16.dp)) {
        EditRadioGroup(
            mItems = kinds,
            selectedGender, setSelected
        )
    }

    return selectedGender
}

@Composable
fun EditRadioGroup(
    mItems: List<String>,
    selectedGender: String,
    setSelected: (selected: String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        mItems.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedGender == item,
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



