package com.example.projecttable

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
import androidx.compose.material3.ExperimentalMaterial3Api
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableInsertScreen(navController: NavHostController){

    var textFieldName by remember { mutableStateOf("") }
    var textFieldID by remember { mutableStateOf("") }
    var textFieldSeat_count by remember { mutableStateOf("") }
    var textFieldPassword by remember { mutableStateOf("") }
    val status = listOf("ว่าง","ไม่ว่าง")
    var (selected , setSelected) = rememberSaveable { mutableStateOf("") }
    val createClient = TableAPI.create()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Insert New Student",
            fontSize = 25.sp
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldName,
            onValueChange = { textFieldName=it },
            label = { Text("กรอกชื่อ/เลขโต๊ะ") }
        )
        Text(text = "เลือกสถานะ:")
        MyRadioButton(mItems = status,selected,setSelected)
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldSeat_count,
            onValueChange = { textFieldSeat_count=it },
            label = { Text("กรอกจำนวนที่นั่ง") }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldPassword,
            onValueChange = { textFieldPassword =it },
            label = { Text("กรอกรหัสผ่านของโต๊ะ") }
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
                    createClient.insertTable(textFieldID,textFieldName,selected,textFieldSeat_count.toInt(),textFieldPassword)
                        .enqueue(object : Callback<Table>{
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
                    textFieldName= ""
                    textFieldSeat_count = ""
                    textFieldPassword = ""

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


@Composable
fun MyRadioButton(mItems:List<String>, selected: String, setSelected:(selected:String)->Unit){
    Column(
        modifier = Modifier
            .padding(start = 16.dp)
    ){
        Text(
            text = "Table Status :",
            textAlign = TextAlign.Start,
            fontSize = 23.sp,
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
