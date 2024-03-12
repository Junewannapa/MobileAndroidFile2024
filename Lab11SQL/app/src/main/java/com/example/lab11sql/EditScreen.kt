package com.example.lab11sql

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(navController: NavHostController){
    val data = navController.previousBackStackEntry?.savedStateHandle?.get<Student>("data")?:
    Student("","","",0)

    val contextForToast = LocalContext.current
    var id by remember { mutableStateOf(data.id) }
    var textFieldName by remember { mutableStateOf(data.name) }
    var textFieldGender by remember { mutableStateOf(data.gender) }
    var textFieldAge by remember { mutableStateOf(data.age.toString()) }


    var showDeleteDialog by remember { mutableStateOf(false) }

    var dbHandler = DatabaseHelper.getInstance(contextForToast)
    dbHandler.writableDatabase

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
            value = id,
            onValueChange = { id = it },
            label = { Text("Student ID") },
            enabled = false,
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldName,
            onValueChange = { textFieldName = it },
            label = { Text("Student Name") },
        )
        EditRadioGroup(
            mItems = listOf("Male", "Female","Other"),
            selected = textFieldGender,
            setSelected = { textFieldGender = it }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldAge,
            onValueChange = { textFieldAge = it },
            label = { Text("Student Age") },
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
                    text = { Text("Do you want to delete this student:${id}?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                val result = dbHandler.deleteStudent(id)
                                navController.navigate(Screen.Home.route)
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
                    val updatedStudent = Student(id, textFieldName, textFieldGender, textFieldAge.toInt())
                    val result = dbHandler.updateStudent(updatedStudent)

                    if (result) {
                        Toast.makeText(contextForToast, "Student updated successfully", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(contextForToast, "Update failed", Toast.LENGTH_SHORT).show()
                    }
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