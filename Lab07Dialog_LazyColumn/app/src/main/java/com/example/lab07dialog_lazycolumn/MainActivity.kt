package com.example.lab07dialog_lazycolumn

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab07dialog_lazycolumn.ui.theme.Lab07Dialog_LazyColumnTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab07Dialog_LazyColumnTheme {
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
    Lab07Dialog_LazyColumnTheme {
        Greeting("Android")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScreen() {
    var studentItemsList = remember { mutableStateListOf<Student>() }
    var contextForToast = LocalContext.current.applicationContext
    var showDialog by remember { mutableStateOf(false) }
    var textFieldID by remember { mutableStateOf("") }
    var textFieldName by remember { mutableStateOf("") }
    var textFieldAge by remember { mutableStateOf("") }
    var deleteDialog by remember { mutableStateOf(false) }
    val hobbyList = listOf("Reading", "Painting", "Cooking")
   var selectedOut by remember { mutableStateOf("") }
    val selectedItems by remember { mutableStateOf(mutableListOf<String>())}
//        val select : String = ""



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
                    text = "Student Lists:",
                    fontSize = 25.sp
                )
            }
            Button(onClick = {
                showDialog = true
            }) {
                Text("Add Student")
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
                            value = textFieldID,
                            onValueChange = { textFieldID = it },
                            label = { Text("Enter your ID") }
                        )
                        OutlinedTextField(
                            value = textFieldName,
                            onValueChange = { textFieldName = it },
                            label = { Text("Enter your name") }
                        )

                        OutlinedTextField(
                            value = textFieldAge,
                            onValueChange = { textFieldAge = it },
                            label = { Text("Enter your age") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number)
                        )
                        Text(text = "Select your hobby:",
                            fontSize = 25.sp)
                        CheckboxGroup(
                            items = hobbyList){ newSelectedItems ->
                            selectedItems.clear()
                            selectedItems.addAll(newSelectedItems)
                            Log.d("CheckboxGroup", "Selected items: $selectedItems")
                            selectedOut = selectedItems.toString()
//                            select = selectedItems.toString()
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            val lastIndex = selectedItems.size - 1
                            var hobbySelect = ""

                            selectedItems.forEachIndexed { index, item ->
                                hobbySelect += if (index == lastIndex) item else "$item, "
                            }
                            studentItemsList.add(Student(textFieldID, textFieldName, textFieldAge,hobbySelect))
                            textFieldID = ""
                            textFieldName = ""
                            textFieldAge = ""

                        }
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                        textFieldID = ""
                        textFieldName = ""
                    }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            var itemClick = Student("", "", "", "")



            itemsIndexed(
                items = studentItemsList,
            ) { index, item ->
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
                        Text(text = "ID: ${item.id} \nName: ${item.name} \nAge: ${item.age} \nHobby: ${item.hobby}")


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
                        text = { Text(text = "Are you sure you want to delete ${itemClick.name} ?")},
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
fun CheckboxGroup(items: List<String>,
                  onSelectionChange: (List<String>) -> Unit) {
    val selectedItems = remember { mutableStateListOf<String>() }

    Column{
        items.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = selectedItems.contains(item),
                    onCheckedChange = {
                        if (it) {
                            selectedItems.add(item)
                        } else {
                            selectedItems.remove(item)
                        }
                        onSelectionChange(selectedItems.toList())
                    }
                )
                Text(text = item, fontSize = 15.sp)
            }
        }


    }
}