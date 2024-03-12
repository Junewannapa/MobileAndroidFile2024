package com.example.ass07


import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ass07.ui.theme.ASS07Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ASS07Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ASS07Theme {
        Greeting("Android")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScreen() {
    var studentItemsList = remember { mutableStateListOf<Register>() }
    var contextForToast = LocalContext.current.applicationContext
    var showDialog by remember { mutableStateOf(false) }
    var textFieldName by remember { mutableStateOf("") }
    var textFieldMail by remember { mutableStateOf("") }
    var textFieldSalary by remember { mutableStateOf("") }
    var deleteDialog by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf("") }



    Column {
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
                    text = "Member Lists:",
                    fontSize = 25.sp
                )
            }
            Button(onClick = {
                showDialog = true
            }) {
                Text("Add Member")
            }

        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Enter Information",
                    fontSize = 25.sp) },
                text = {

                    Column {
                        OutlinedTextField(
                            value = textFieldName,
                            onValueChange = { textFieldName = it },
                            label = { Text("Enter your Name") }
                        )
                        Text(text = "Gender:",
                            fontSize = 20.sp)
                        RadioGroupUsage { gender ->
                            selectedGender = gender
                        }
                        OutlinedTextField(
                            value = textFieldMail,
                            onValueChange = { textFieldMail = it },
                            label = { Text("Enter your e-mail") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email)
                        )
                            OutlinedTextField(
                                value = textFieldSalary,
                                onValueChange = { textFieldSalary = it },
                                label = { Text("Enter your Salary") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number)
                            )
                        }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false

                            val salary = textFieldSalary.toIntOrNull() ?: 0

                            studentItemsList.add(Register(textFieldName, textFieldMail, salary, selectedGender))
                            textFieldName = ""
                            textFieldMail = ""
                            textFieldSalary = ""
                        }
                    ) {
                        Text("Register")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                        textFieldName = ""
                        textFieldMail = ""
                    }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            var itemClick = Register("", "", 0,"")

            itemsIndexed(
                items = studentItemsList,
            ) { index, item ->
                Card(
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 5.dp)
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
                            contextForToast,
                            "Click on ${item.name}.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dp(120f))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Name: ${item.name} \nGenger: ${item.gender} \nE-mail: ${item.mail} \nSalary: ${item.salary}")


                        TextButton(
                            onClick = {
                                itemClick = item
                                deleteDialog = true
                            }
                        ) {
                            Text("Delete")
                        }
                    }
                }

                if (deleteDialog) {
                    AlertDialog(
                        title = {Text(text = "Warning")},
                        text = { Text(text = "Do you want to delete this member: ${itemClick.name} ?")},
                        onDismissRequest = {
                            deleteDialog = false
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    deleteDialog = false
                                    Toast.makeText(
                                        contextForToast,
                                        "Yes, ${itemClick.name} is deleted",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    studentItemsList.remove(itemClick)
                                }
                            ) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    deleteDialog = false
                                }
                            ) {
                                Text("No")
                            }
                        }
                    )
                }
            }
        }

    }
}

@Composable
fun MyRadioGroup(mItems: List<String>, selected: String, setSelected: ( selected: String)->Unit,){
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        mItems.forEach{
                item ->
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selected == item ,
                    onClick = {
                        setSelected(item) },
                    enabled = true,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Magenta)
                )
                Text(
                    text = item,
                    modifier = Modifier.padding(start = 1.dp))
            }
        }
    }
}


@Composable
fun RadioGroupUsage(onGenderSelected: (String) -> Unit){
    val kinds = listOf("Male", "Female", "Other")
    var selectedGender by remember { mutableStateOf("") }

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()

        ) {
            MyRadioGroup(
                mItems = kinds,
                selected = selectedGender,
                setSelected = { gender ->
                    selectedGender = gender
                    onGenderSelected(gender)
                }
            )
        }
    }
}