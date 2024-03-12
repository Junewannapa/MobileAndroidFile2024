package com.example.lab10_653380011_7

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController){
    val contextForToast = LocalContext.current
    val createClient = StudentAPI.create()
    var studentID by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var studentName by remember { mutableStateOf("") }
    val gender = listOf("Male","Female","Other")
    var (selected , setSelected) = rememberSaveable { mutableStateOf("") }

    var isButtonEnabled by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center ,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Register",
            fontSize = 25.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = studentID,
            onValueChange = {
                studentID = it
                isButtonEnabled= validateInput(studentID, password)
            },
            label = { Text("Student ID") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
            },
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = studentName,
            onValueChange = {
                studentName = it
                isButtonEnabled= validateInput(studentName, password)
            },
            label = { Text("Name") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
            },
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        MyRadioButton(mItems = gender,selected,setSelected)


        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {
               password = it
                isButtonEnabled= validateInput(studentID, password)
            },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
            },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            keyboardController?.hide()
            focusManager.clearFocus()
            createClient.registerStudent(
                studentID,studentName,password,selected
            ).enqueue(object : Callback<LoginClass> {
                override fun onResponse(
                    call: Call<LoginClass>,
                    response: Response<LoginClass>
                ){
                    if (response.isSuccessful) {
                        Toast.makeText(contextForToast, "Successfully Inserted", Toast.LENGTH_LONG).show()
                        if (navController.currentBackStack.value.size > 2) {
                            navController.popBackStack()
                        } else {
                            navController.navigate(Screen.Login.route)
                        }
                    } else {
                        Toast.makeText(contextForToast, "Inserted Failure", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<LoginClass>, t: Throwable) {
                    Toast.makeText(contextForToast, "Error onFailure" + t.message, Toast.LENGTH_LONG).show()
                }
            })
        },
            enabled = isButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
            ) {
            Text("Register")
        }
    }


}

fun validateInput(studentID: String, password: String): Boolean {
    return !studentID.isNullOrEmpty() && !password.isNullOrEmpty()
}

@Composable
fun MyRadioButton(mItems:List<String>, selected: String, setSelected:(selected:String)->Unit){
    Column(
        modifier = Modifier
            .padding(start = 16.dp)
    ){
        Text(
            text = "Student Gender :",
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
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
                            selectedColor = Color.Magenta
                        )
                    )
                    Text(text = item, modifier = Modifier.padding(start = 5.dp))
                }
            }
        }
    }
}
