package com.example.a653380011_7_cocoa

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
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
    val createClient = OrderAPI.create()
    val contectForToast = LocalContext.current.applicationContext
    var textFieldName by remember {
        mutableStateOf("")
    }
    var textFieldNumber by remember {
        mutableStateOf("")
    }
    var salary: Int = 0;
    var sum: Int = 0;
    val size = listOf("S","M","L","XL")
    var sweet by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var (selected,setSelected) = remember {
        mutableStateOf("")
    }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(25.dp))
        Text(text = "Insert new order", fontSize = 25.sp)

        OutlinedTextField(value = textFieldName,
            onValueChange ={textFieldName=it},
            label = { Text(text = "Customer Name")},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) )

        GlassSize(Items = size, selected,setSelected)
        sweet = SweetDropdown()
        OutlinedTextField(value = textFieldNumber,
            onValueChange ={textFieldNumber=it},
            label = { Text(text = "Number of glass")},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) )

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Button(modifier = Modifier.width(130.dp),onClick = {
                if(sweet == "0%"){
                    sweet = "0"
                }else if(sweet == "25%"){
                    sweet = "25"
                }else if(sweet == "50%"){
                    sweet = "50"
                }else if(sweet == "75%"){
                    sweet = "75"
                }else  if(sweet == "100%"){
                    sweet = "100"
                }

            if(selected == "S"){
                salary = 30
            }else if(selected == "M"){
                salary = 50
            }else if(selected == "L"){
                salary = 70
            }else if(selected == "XL"){
                salary = 90
            }
                sum = salary * textFieldNumber.toInt()
                price = sum.toString()
                createClient.insertOrder(textFieldName,selected,textFieldNumber.toInt(),sweet.toInt(),price.toInt(),)
                    .enqueue(object :Callback<Order>{
                        override fun onResponse(call: Call<Order>, response: Response<Order>){
                            if (response.isSuccessful){
                                Toast.makeText(contectForToast,"Successfully Inserted ", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(contectForToast,"Inserted Failed ", Toast.LENGTH_LONG).show()
                            }
                        }
                        override fun onFailure(call: Call<Order>, t: Throwable){
                            Toast.makeText(contectForToast,"Error onFailure "+t.message, Toast.LENGTH_LONG).show()
                        }
                    })
                textFieldName=""
                textFieldNumber=""
                setSelected("")
                navController.navigateUp()
            }) {
                Text(text = "Save")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(modifier = Modifier.width(130.dp),onClick = {
                textFieldName=""
                textFieldNumber=""
                setSelected("")
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
fun GlassSize(Items: List<String>,selected: String, setSelected: (selected:String)->Unit){
    Column(Modifier .fillMaxWidth() .padding(16.dp)){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
        ) {
            Text(text = "Glass size: $selected",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 5.dp))
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SweetDropdown(): String {
    val keyboardController = LocalSoftwareKeyboardController.current

    val sweetList = listOf(
        "Select Subject",
        "0%",
        "25%",
        "50%",
        "75%",
        "100%"
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedSweet by remember { mutableStateOf(sweetList[0]) }

    ExposedDropdownMenuBox(
        modifier = Modifier.clickable { keyboardController?.hide() },
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .menuAnchor()
                .clickable { keyboardController?.hide() },
            textStyle = TextStyle.Default.copy(fontSize = 16.sp),
            readOnly = true,
            value = selectedSweet,
            onValueChange = {},
            label = { Text(text = "Sweet") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
            colors = ExposedDropdownMenuDefaults.textFieldColors(),


            )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        )
        {
            sweetList.forEach { selectionOption ->
                DropdownMenuItem(
                    text={Text(selectionOption)},
                    onClick = {
                        selectedSweet = selectionOption
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }

    return selectedSweet
}
