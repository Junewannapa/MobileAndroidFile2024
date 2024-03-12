package com.example.admintable

import android.annotation.SuppressLint
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("RestrictedApi")
@Composable
fun TableInsertScreen(navController: NavHostController) {

    var textFieldNumber by remember { mutableStateOf("") }
    var textFieldName by remember { mutableStateOf("") }
    //var textFieldStatus by remember { mutableStateOf("") }
    var textFieldSeat_count by remember { mutableStateOf("") }
    var textFieldPassword by remember { mutableStateOf("12345678") }
    var selectedStatus by remember { mutableStateOf("ว่าง") }
//    val status = listOf("ว่าง","ไม่ว่าง")
//    var (selected , setSelected) = rememberSaveable { mutableStateOf("") }
    val createClient = TableAPI.create()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Insert New Table",
            fontSize = 25.sp
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldName,
            onValueChange = { textFieldName=it },
            label = { Text("Table Name") }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldNumber,
            onValueChange = { textFieldNumber=it },
            label = { Text("Table Number") }
        )
//        MyRadioButton(mItems = status,selected,setSelected)
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldSeat_count,
            onValueChange = { textFieldSeat_count=it },
            label = { Text("Table Seat Count") }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldPassword,
            onValueChange = { textFieldPassword=it },
            label = { Text("Table Password") },
            enabled = false,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        )
        {
            Button(modifier = Modifier
                .width(130.dp),
                onClick = {
                    createClient.insertTable(textFieldName,textFieldNumber,selectedStatus,textFieldSeat_count.toInt(),textFieldPassword)
                        .enqueue(object : Callback<Table> {
                            override fun onResponse(
                                call: Call<Table>,
                                response: Response<Table>
                            ) {
                            }
                            override fun onFailure(call: Call<Table>, t: Throwable) {
                            }
                        })
                    navController.navigateUp()
                }) {
                Text("Save")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(modifier = Modifier
                .width(130.dp),
                onClick = {
                    textFieldNumber=""
                    textFieldName=""
                    if (navController.currentBackStack.value.size >= 2){
                        navController.popBackStack()
                    }
                    navController.navigate(Screen.TableHome.route)
                }){
                Text("Cancel")
            }
        }
    }
}

//@Composable
//fun MyRadioButton(mItems:List<String>, selected: String, setSelected:(selected:String)->Unit){
//    Column(
//        modifier = Modifier
//            .padding(start = 16.dp)
//    ){
//        Text(
//            text = "Table :",
//            textAlign = TextAlign.Start,
//            fontSize = 23.sp,
//            modifier = Modifier
//                .fillMaxWidth()
//        )
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            mItems.forEach { item ->
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    RadioButton(
//                        selected = selected == item,
//                        onClick = {
//                            setSelected(item)
//                        },
//                        enabled = true,
//                        colors = RadioButtonDefaults.colors(
//                            selectedColor = Color.Magenta
//                        )
//                    )
//                    Text(text = item, modifier = Modifier.padding(start = 5.dp))
//                }
//            }
//        }
//    }
//}