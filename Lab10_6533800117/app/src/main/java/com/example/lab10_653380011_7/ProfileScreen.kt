package com.example.lab10_653380011_7

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun ProfileScreen(navController: NavHostController){
    lateinit var sharedPreferences: SharedPreferencesManager
    val contextForToast = LocalContext.current.applicationContext
    sharedPreferences = SharedPreferencesManager(contextForToast)
    val userId = sharedPreferences.userId?:""
    val createClient = StudentAPI.create()
    val initialStudent = ProfileClass("","","","")
    var studentItems by remember { mutableStateOf(initialStudent) }

//    Check Lifecycle State
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    var logoutDialog by remember { mutableStateOf(false) }
    var checkedState by remember { mutableStateOf(false) }

    LaunchedEffect(lifecycleState){
        when (lifecycleState){
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {}
            Lifecycle.State.RESUMED -> {
                createClient.searchStudent(userId)
                    .enqueue(object: Callback<ProfileClass> {
                        override fun onResponse(call: Call<ProfileClass>,
                                                response: Response<ProfileClass>){
                            if(response.isSuccessful){
                                studentItems = ProfileClass(
                                    response.body()!!.std_id,
                                    response.body()!!.std_name,
                                    response.body()!!.std_gender,
                                    response.body()!!.role
                                )
                            }else{
                                Toast.makeText(contextForToast,"Student ID Not Found", Toast.LENGTH_LONG).show()
                            }
                        }
                        override fun onFailure(call: Call<ProfileClass>,t: Throwable){
                            Toast.makeText(contextForToast, "Error onFailure" + t.message,Toast.LENGTH_LONG).show()
                        }
                    })
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Profile",
            fontSize = 25.sp
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Student ID : $userId\nName: ${studentItems.std_name}\n"
                    +"Gender : ${studentItems.std_gender}\nRole: ${studentItems.role}",
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        val isInvisible = studentItems.role == "admin"
        Box (
            content = {
                if(isInvisible){
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        onClick = {
                            navController.navigate(Screen.Home.route)
                        }
                    ) {
                        Text(text = "Show all students")
                    }
                }
            },
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                  logoutDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Logout")
        }
    }
    if(logoutDialog){
        AlertDialog(
            onDismissRequest = { logoutDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    logoutDialog = false
                    if (checkedState) {
                        sharedPreferences.clearUserLogin()
                        Toast.makeText(contextForToast, "Clear User Login", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        sharedPreferences.clearUserAll()
                        Toast.makeText(
                            contextForToast,
                            "Clear User Login and e-mail",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (navController.currentBackStack.value.size >= 2) {
                        navController.popBackStack()
                    }
                    navController.navigate(Screen.Login.route)
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {

                        logoutDialog = false
                        Toast.makeText(contextForToast, "Click on No", Toast.LENGTH_SHORT).show()
                    },
                ) {
                    Text("No")
                }
            },
            title = { Text("Logout") },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Are you sure you want to log out?")
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start) {
                        Checkbox(
                            checked = checkedState,
                            onCheckedChange ={ isChecked ->
                                checkedState = isChecked
                            } )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text("Remember my student id")
                    }
                }
            },
        )
    }
}