package com.example.a653380011_7_cocoa

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Response

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController){
    val createClient = OrderAPI.create()
    var orderItemsList = remember { mutableStateListOf<Order>() }
    val contextForToast = LocalContext.current.applicationContext

    orderItemsList.clear()
    createClient.retrieveOrder()
        .enqueue(object : retrofit2.Callback<List<Order>>{
            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>){
                response.body()?.forEach{
                    orderItemsList.add(Order(it.id,it.customer,it.glass_size,it.number_of_glass,it.sweet,it.price))
                }
            }
            override fun onFailure(call: Call<List<Order>>, t: Throwable){
                Toast.makeText(contextForToast,"Error onFailure "+t.message,Toast.LENGTH_LONG).show()
            }
        })
    Column {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Button(onClick = {
                if (navController.currentBackStack.value.size >= 2){
                    navController.popBackStack()
                }
                navController.navigate(Screen.Insert.route)
            },Modifier.fillMaxWidth()) {
                Text(text = "Insert Order", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(15.dp))
                Text(text = "Cocoa Order Lists:", fontSize = 30.sp)

        }


        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)){
            itemsIndexed(
                items = orderItemsList,
            ){ _, item ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .height(Dp(130f))
                        .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically)
                    {
                        Text(text = "Customer name: ${item.customer}\n" +
                                    "Glass size: ${item.glass_size}    Sweet: ${item.sweet}%\n"+
                                    "Number of glass: ${item.number_of_glass}\n"+
                                    "Price: ${item.price}",
                            fontSize = 18.sp)
                    }
                }

        }
    }
}