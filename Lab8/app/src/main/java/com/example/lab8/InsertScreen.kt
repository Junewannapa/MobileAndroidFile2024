package com.example.lab8

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
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertScreen(navController: NavHostController){
    val createClient = StudentAPI.create()
    val contectForToast = LocalContext.current.applicationContext
    var textFirldID by remember {
        mutableStateOf("")
    }
    var textFieldName by remember {
        mutableStateOf("")
    }
    var textFieldAge by remember {
        mutableStateOf("")
    }
    val kinds = listOf("Male","Famale","Other")
    var (selected,setSelected) = remember {
        mutableStateOf("")
    }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(25.dp))
        Text(text = "Insert New Student", fontSize = 25.sp)
        OutlinedTextField(value = textFirldID, onValueChange ={textFirldID=it}, label = { Text(text = "Student ID")}, modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp) )
        OutlinedTextField(value = textFieldName, onValueChange ={textFieldName=it}, label = { Text(text = "Student Name")}, modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp) )
        ///
        Gender(Items = kinds, selected,setSelected)
        OutlinedTextField(value = textFieldAge, onValueChange ={textFieldAge=it}, label = { Text(text = "Student Age")}, modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp) )
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Button(modifier = Modifier.width(130.dp),onClick = { 
                ///
                createClient.insertStd(textFirldID,textFieldName,selected,textFieldAge.toInt())
                    .enqueue(object :Callback<Student>{
                        override fun onResponse(call: Call<Student>, response: Response<Student>){
                            if (response.isSuccessful){
                                Toast.makeText(contectForToast,"Successfully Inserted ", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(contectForToast,"Inserted Failed ", Toast.LENGTH_LONG).show()
                            }
                        }
                        override fun onFailure(call: Call<Student>, t: Throwable){
                            Toast.makeText(contectForToast,"Error onFailure "+t.message, Toast.LENGTH_LONG).show()
                        }
                    })
                textFirldID=""
                textFieldName=""
                textFieldAge=""
                setSelected("")
                navController.navigateUp()
            }) {
                Text(text = "Save")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(modifier = Modifier.width(130.dp),onClick = { 
                textFirldID=""
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
fun Gender(Items: List<String>,selected: String, setSelected: (selected:String)->Unit){
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
        ) {
            Text(text = "Gender: $selected", textAlign = TextAlign.Start, modifier = Modifier.padding(start = 5.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Items.forEach { item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selected == item,
                        onClick = { setSelected(item) },
                        enabled = true,
                        colors = RadioButtonDefaults.colors(selectedColor = Color.Magenta)
                    )
                    Text(text = item)
                }
            }
        }
    }
}