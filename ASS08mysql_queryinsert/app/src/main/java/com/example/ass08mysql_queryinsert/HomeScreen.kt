package com.example.ass08mysql_queryinsert

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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val createClient = EmployeeAPI.create()
    var employeeItemsList = remember { mutableStateListOf<Employee>() }
    val contextForToast = LocalContext.current.applicationContext

    var textFieldName by remember { mutableStateOf("") }

    // Check Lifecycle State
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {

        when (lifecycleState) {
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {}
            Lifecycle.State.RESUMED -> {
                showAllData(employeeItemsList, contextForToast)
            }
        }
    }
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
                    text = "Employee Lists:",
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
                Text(text = "Add Employee")
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            var itemClick = Employee( 0,  "",  "",  "",0)
            itemsIndexed(
                items = employeeItemsList,
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
                                contextForToast, "Click on ${item.emp_name}.",
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
                                text = "Name: ${item.emp_name}\n" +
                                        "Gender: ${item.emp_gender}\n" +
                                        "Email: ${item.emp_email}\n" +
                                        "Salary: ${item.emp_salary} Baht",
                                Modifier.weight(0.85f)
                            )
                            TextButton(onClick = {
                                val itemClick = item
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "data",
                                    Employee(
                                        item.emp_id,
                                        item.emp_name,
                                        item.emp_gender,
                                        item.emp_email,
                                        item.emp_salary
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
fun showAllData(employeeItemsList: MutableList<Employee>, context: Context) {
    val createClient = EmployeeAPI.create()
    createClient.retriveEmployee()
        .enqueue(object : Callback<List<Employee>> {
            override fun onResponse(
                call: Call<List<Employee>>,
                response: Response<List<Employee>>
            ) {
                employeeItemsList.clear()
                response.body()?.forEach {
                    employeeItemsList.add(
                        Employee(
                            it.emp_id,
                            it.emp_name,
                            it.emp_gender,
                            it.emp_email,
                            it.emp_salary
                        )
                    )
                }
            }

            override fun onFailure(call: Call<List<Employee>>, t: Throwable) {
                Toast.makeText(context, "Error onFailure " + t.message, Toast.LENGTH_LONG)
                    .show()
            }
        })

}
