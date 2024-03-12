package com.example.lab10_653380011_7


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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navHostController: NavHostController){
    var studentID by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var isButtonEnabled by remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    val keyboadrdController = LocalSoftwareKeyboardController.current

    val createClient = StudentAPI.create()
    val contextForToast = LocalContext.current.applicationContext
    var studentItems = remember {
        mutableStateListOf<LoginClass>()
    }
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    sharedPreferencesManager = SharedPreferencesManager(context = contextForToast)

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {

        when (lifecycleState) {
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {}
            Lifecycle.State.RESUMED -> {
                if (sharedPreferencesManager.isLoggedIn){
                    navHostController.navigate(Screen.Profile.route)
                }
                if (!sharedPreferencesManager.userId.isNullOrEmpty()){
                    studentID= sharedPreferencesManager.userId?:"1"
                }
            }
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Log in",
            fontSize = 25.sp)
        Spacer(modifier = Modifier.height(16.dp))
       OutlinedTextField(
            value = studentID,
            onValueChange = {studentID=it
                isButtonEnabled=!studentID.isNullOrEmpty()&&!password.isNullOrEmpty()
            },
            label = { Text(text = "Student ID")},
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {password=it
                isButtonEnabled=!studentID.isNullOrEmpty()&&!password.isNullOrEmpty()
            },
            label = { Text(text = "Password")},
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            keyboadrdController?.hide()
            focusManager.clearFocus()
            createClient.loginStudent(studentID,password)
                .enqueue(
                    object :Callback<LoginClass>{
                        @SuppressLint("RestrictedApi")
                        override fun onResponse(
                            call: Call<LoginClass>,
                            response: Response<LoginClass>
                        ) {
                            if (response.isSuccessful){
                                if (response.body()!!.success==1){
                                    sharedPreferencesManager.isLoggedIn=true
                                    sharedPreferencesManager.userId= response.body()!!.std_id.toString()
                                    Toast.makeText(contextForToast,"Login successful ",Toast.LENGTH_LONG).show()
                                    if (navHostController.currentBackStack.value.size>=2){
                                        navHostController.popBackStack()
                                    }
                                    navHostController.navigate(Screen.Profile.route)
                                }else{
                                    Toast.makeText(contextForToast,"Student or password is incorrect.",Toast.LENGTH_LONG).show()
                                }
                            }else{
                                studentItems.clear()
                                Toast.makeText(contextForToast,"Student ID not found",Toast.LENGTH_LONG).show()

                            }
                        }

                        override fun onFailure(call: Call<LoginClass>, t: Throwable) {
                            Toast.makeText(contextForToast,"Error onFailure"+t.message,Toast.LENGTH_LONG).show()
                        }
                    }
                )
        },
            enabled = isButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Login")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row (
            Modifier
                .fillMaxWidth()
                .padding(5.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(text = "Don't have an account?")
            TextButton(onClick = {
                if (navHostController.currentBackStack.value.size>=2){
                    navHostController.popBackStack()
                }
                navHostController.navigate(Screen.Register.route)
            }) {
                Text(text = "Register")
            }
        }

    }
}