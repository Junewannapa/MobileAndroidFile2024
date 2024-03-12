package com.example.ass08

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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertScreen(navController: NavController) {
    val contextForToast = LocalContext.current
    val createClient = EmployeeAPI.create()
    var empItemsList = remember { mutableStateListOf<Employee>() }
    var textFieldName by remember { mutableStateOf("") }
    var textFieldEmail by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var textFieldSalary by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Insert New Employee",
            fontSize = 25.sp
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldName,
            onValueChange = { textFieldName = it },
            label = { Text("Name") }
        )
    Column(
        modifier = Modifier
            .align(Alignment.Start)
            .padding(16.dp)

    ) {
        Text(
            text = "Gender:",
            fontSize = 20.sp,

            )
        RadioGroupUsage { gender ->
            selectedGender = gender
        }
    }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldEmail,
            onValueChange = { textFieldEmail = it },
            label = { Text("E-mail") }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldSalary,
            onValueChange = { textFieldSalary = it },
            label = { Text("Salary") }
        )

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center)
        {
            Button(modifier = Modifier.width(130.dp),onClick = {
                ///
                createClient.insertEmp(textFieldName,selectedGender,textFieldEmail,textFieldSalary.toInt())
                    .enqueue(object :Callback<Employee>{
                        override fun onResponse(call: Call<Employee>, response: Response<Employee>){
                            if (response.isSuccessful){
                                Toast.makeText(contextForToast,"Successfully Inserted ", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(contextForToast,"Inserted Failed ", Toast.LENGTH_LONG).show()
                            }
                        }
                        override fun onFailure(call: Call<Employee>, t: Throwable){
                            Toast.makeText(contextForToast,"Error onFailure "+t.message, Toast.LENGTH_LONG).show()
                        }
                    })
                textFieldName=""
                textFieldEmail=""
                selectedGender=""

                navController.navigateUp()
            }) {
                Text(text = "Save")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(modifier = Modifier.width(130.dp),onClick = {
                textFieldName=""
                if(navController.currentBackStack.value.size >= 2){
                    navController.popBackStack()
                }
                navController.navigate(Screen.Home.route)
            }) {
                Text(text = "Cancel")
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
                    modifier = Modifier.padding(start = 9.dp))
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