package com.example.lab8mysql_queryinsert

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val createClient = StudentAPI.create()
    var studentItemsList = remember { mutableStateListOf<Student>() }
    val contextForToast = LocalContext.current.applicationContext
    var textFieldID by remember { mutableStateOf("") }

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {

        when (lifecycleState) {
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {}
            Lifecycle.State.RESUMED -> {
                showAllData(studentItemsList, contextForToast)
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
                value = textFieldID,
                onValueChange = { textFieldID = it },
                label = { Text(text = "Student ID") }
            )
            Button(onClick = {
                if (textFieldID.trim().isEmpty()) {
                    showAllData(studentItemsList, contextForToast)
                } else {
                    studentItemsList.clear()
                    createClient.retrieveStudentID(textFieldID)
                        .enqueue(object : Callback<Student> {
                            override fun onResponse(
                                call: Call<Student>,
                                response: Response<Student>
                            ) {
                                if (response.isSuccessful) {
                                    studentItemsList.add(
                                        Student(
                                            response.body()!!.std_id,
                                            response.body()!!.std_name,
                                            response.body()!!.std_gender,
                                            response.body()!!.std_age
                                        )
                                    )
                                    Toast.makeText(
                                        contextForToast,
                                        "Student ID Found", Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        contextForToast,
                                        "Student Not ID Found", Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<Student>, t: Throwable) {
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
                    text = "Student Lists: ${studentItemsList.size} ",
                    fontSize = 25.sp
                )
            }
            Button(onClick = {
                if (navController.currentBackStack.value.size >= 2) {
                    navController.popBackStack()
                } else {
                    navController.navigate(Screen.Insert.route)
                }
            }) {
                Text(text = "Add Student")
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            var itemClick = Student( "",  "",  "",  0)
            itemsIndexed(
                items = studentItemsList,
                itemContent = { index, item ->
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .fillMaxWidth()
                            .height(130.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        ),
                        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                        onClick = {
                            Toast.makeText(
                                contextForToast, "Click on ${item.std_name}.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(Dp(value = 130f))
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ID: ${item.std_id}\n" +
                                        "Name: ${item.std_name}\n" +
                                        "Gender: ${item.std_gender}\n" +
                                        "Age: ${item.std_age}",
                                Modifier.weight(0.85f)
                            )
                            TextButton(onClick = {
                                val itemClick = item
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "data",
                                    Student(
                                        item.std_id,
                                        item.std_name,
                                        item.std_gender,
                                        item.std_age
                                    )
                                )
                                navController.navigate(Screen.Edit.route)
                            }
                            )
                            {
                                Text(text = "Edit/Delete")
                            }
                        }
                    }
                })   }   }   }
fun showAllData(studentItemsList: MutableList<Student>, context: Context) {
    val createClient = StudentAPI.create()
    createClient.retrieveStudent()
        .enqueue(object : Callback<List<Student>> {
            override fun onResponse(
                call: Call<List<Student>>,
                response: Response<List<Student>>
            ) {
                studentItemsList.clear()
                response.body()?.forEach {
                    studentItemsList.add(
                        Student(
                            it.std_id,
                            it.std_name,
                            it.std_gender,
                            it.std_age
                        )
                    )
                }
            }

            override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                Toast.makeText(context, "Error onFailure " + t.message, Toast.LENGTH_LONG)
                    .show()
            }
        })

}