package com.example.tabletap

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tabletap.ui.theme.TableTapTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("RestrictedApi")
@Composable
fun LoginScreen(navHostController : NavHostController){
    var username by remember {
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

    val createClient = LoginAPI.create()
    val contextForToast = LocalContext.current.applicationContext
    var userItems = remember {
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
                    return@LaunchedEffect
                }
                if (!sharedPreferencesManager.userId.isNullOrEmpty()){
                    username = sharedPreferencesManager.userId?:"1"
                }
            }
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Log in", fontSize = 25.sp)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = {username=it
                isButtonEnabled=!username.isNullOrEmpty()&&!password.isNullOrEmpty()
            },
            label = { Text(text = "Username")},
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
                isButtonEnabled=!username.isNullOrEmpty()&&!password.isNullOrEmpty()
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
        Button(
            onClick = {
                keyboadrdController?.hide()
                focusManager.clearFocus()
                createClient.login(username, password).enqueue(
                    object : Callback<LoginClass> {
                        override fun onResponse(
                            call: Call<LoginClass>,
                            response: Response<LoginClass>
                        ) {
                            if (response.isSuccessful) {
                                val loginClass = response.body()
                                if (loginClass != null && loginClass.success == 1) {
                                    val role = loginClass.role
                                    if (role == "admin") {
                                        sharedPreferencesManager.isLoggedIn=true
                                        sharedPreferencesManager.role = loginClass.role
                                        val intent = Intent(contextForToast, MainActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                        contextForToast.startActivity(intent)

                                    } else if (role == "table") {
                                        sharedPreferencesManager.isLoggedIn=true
                                        val intent = Intent(contextForToast, MainActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                        contextForToast.startActivity(intent)

                                        Toast.makeText(contextForToast, "Login table role Success", Toast.LENGTH_LONG).show()
                                    }
                                } else {
                                    Toast.makeText(contextForToast, "Username or password is incorrect.", Toast.LENGTH_LONG).show()
                                }
                            } else {
                                userItems.clear()
                                Toast.makeText(contextForToast,"Username not found",Toast.LENGTH_LONG).show()                            }
                        }

                        override fun onFailure(call: Call<LoginClass>, t: Throwable) {
                            Toast.makeText(contextForToast, "Error onFailure: ${t.message}", Toast.LENGTH_LONG).show()
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

class LoginActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            var context = LocalContext.current.applicationContext
            lateinit var shar : SharedPreferencesManager
            shar = SharedPreferencesManager(context)
            TableTapTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if(shar.isLoggedIn){
                        finish()
                    }else{
                        LodinAndSign()
                    }
                }
            }
        }
    }
}

@Composable
fun LodinAndSign(){
    val nav = rememberNavController()
    var context = LocalContext.current.applicationContext
    NavHost(
        navController = nav ,
        startDestination = Screen.Login.route,){
        composable(
            route = Screen.Login.route
        ) {
            LoginScreen(nav)
        }
        composable(
            route = Screen.Register.route
        ) {
            RegisterScreen(nav)
        }
    }
}
fun starLogin(c: Context){
    val intent = Intent(c, LoginActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    c.startActivity(intent)
}