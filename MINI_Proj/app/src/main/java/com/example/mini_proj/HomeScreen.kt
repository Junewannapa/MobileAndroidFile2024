package com.example.mini_proj

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter

import com.example.mini_proj.Food
import com.example.mini_proj.FoodAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var textFieldID by remember { mutableStateOf("") }
    var textFieldName by remember { mutableStateOf("") }
    var textFieldprice by remember { mutableStateOf("") }
    var quantities = remember { mutableStateMapOf<Int, Int>() }
    var textFieldordernumber by remember { mutableStateOf("") }
    var textFieldtype by remember { mutableStateOf("") }
    var foodItemsList = remember { mutableStateListOf<Food>() }
    val createClient = FoodAPI.create()
    val contextForToast = LocalContext.current
    var foodList = remember { mutableStateListOf<Food>() }

    foodList.clear()
    createClient.retriveFood()
        .enqueue(object : Callback<List<Food>> {
            override fun onResponse(call: Call<List<Food>>, response: Response<List<Food>>) {
                response.body()?.let { foods ->
                    foodList.addAll(foods.filter { it.food_type == "1" })
                }
            }

            override fun onFailure(call: Call<List<Food>>, t: Throwable) {
                Toast.makeText(
                    contextForToast, "Error on Failure" + t.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Column(modifier = Modifier.weight(0.85f)) {
                Text(
                    "หน้ากดเล่นเฉยๆ",
                    fontSize = 25.sp
                )
            }
        }
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Adaptive(minSize = 140.dp),
                verticalArrangement = Arrangement.spacedBy(space = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 5.dp),
                contentPadding = PaddingValues(all = 10.dp)
            ) {
                itemsIndexed(
                    items = foodList.filter { it.food_type == "1" }, // This line filters the list
                ) { index, item ->
                    Card(
                        modifier = Modifier
                            .width(190.dp)
                            .wrapContentHeight()
                            .padding(horizontal = 5.dp, vertical = 5.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        ),
                        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                        onClick = {
                            Toast.makeText(
                                contextForToast, "${item.food_name}.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Column(
                            Modifier
                                .width(190.dp)
                                .wrapContentHeight()
                                .padding(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(item.food_pic),
                                contentDescription = item.food_name,
                                modifier = Modifier
                                    .height(150.dp)
                                    .width(150.dp)
                            )
                            Text(
                                text = "${item.food_name}\t\t\t${item.food_price}.-",
                                textAlign = TextAlign.Center
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Decrement button
                                Button(onClick = {
                                    val currentQuantity = quantities[item.food_quanti] ?: 0
                                    if (currentQuantity > 0) {
                                        quantities[item.food_quanti] = currentQuantity - 1
                                    }
                                }) {
                                    Text("-")
                                }

                                // Quantity text
                                Text("${quantities[item.food_quanti] ?: 0}")

                                // Increment button
                                Button(onClick = {
                                    val currentQuantity = quantities[item.food_quanti] ?: 0
                                    quantities[item.food_quanti] = currentQuantity + 1
                                }) {
                                    Text("+")
                                }
                            }
                        }

                    }
                }
            }

        }
    }