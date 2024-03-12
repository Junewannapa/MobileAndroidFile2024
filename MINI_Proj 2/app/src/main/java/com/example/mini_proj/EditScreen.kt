package com.example.mini_proj

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(navController: NavHostController) {
    val foodId = navController.currentBackStackEntry?.arguments?.getString("foodId")
    var foodItem by remember { mutableStateOf<Food?>(null) }
    var quantity by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(true) } // เพิ่มตัวแปรเก็บค่าสถานะการโหลด

    val createClient = FoodAPI.create()

    // Fetch food details
    LaunchedEffect(foodId) {
        foodId?.let {
            createClient.retrieveFoodById(it).enqueue(object : Callback<Food> {
                override fun onResponse(call: Call<Food>, response: Response<Food>) {
                    if (response.isSuccessful) {
                        foodItem = response.body()
                        quantity = foodItem?.food_quanti?.toInt() ?: 0
                        loading = false
                    }
                }
                override fun onFailure(call: Call<Food>, t: Throwable) {
                    loading = false
                    // Handle failure, for example, by showing a toast
                }
            })
        }
    }


    // Function to update quantity in the backend when + or - is pressed
    // Function to update quantity in the backend when + or - is pressed
    fun updateQuantity(foodId: String, newQuantity: Int) {
        // Ensure quantity is not negative
        val safeNewQuantity = maxOf(newQuantity, 0)

        // Create a Map to hold the body of the request
        val requestBody = mapOf("food_quantity" to safeNewQuantity.toString())

        // Perform the Retrofit call to update the food quantity
        createClient.updateFoodQuantity(foodId, requestBody).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    // Update the quantity state to reflect the new quantity
                    foodItem?.let { food ->
                        foodItem = food.copy(food_quanti = safeNewQuantity)
                    }
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                // Handle failure, for example, by showing a toast
            }
        })
    }


    Scaffold(
        topBar = {
            foodItem?.let { food ->
                CenterAlignedTopAppBar(
                    title = { Text(food.food_name) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        },
        content = { innerPadding ->
            if (loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator() // แสดง Indicator ในระหว่างโหลด
                }
            } else {
                foodItem?.let { food ->
                    EditFoodContent(food, quantity, { newQuantity ->
                        updateQuantity(food.toString(), newQuantity)
                    }, innerPadding)
                    Button(
                        onClick = {
                            // Navigate to the Cart screen
                            navController.navigate(Screen.Cart.route)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp) // Add padding as needed
                    ) {
                        Text("Add to Cart")
                    }
                }
            }
        }
    )
}

@Composable
fun EditFoodContent(
    food: Food,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    padding: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberImagePainter(data = food.food_pic),
            contentDescription = food.food_name,
            modifier = Modifier.size(150.dp) // Set the size as needed
        )
        Text(
            text = food.food_name,
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        Text(
            text = "${food.food_price} บาท",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        QuantitySelector(quantity, onQuantityChange)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun QuantitySelector(quantity: Int, onQuantityChange: (Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { if (quantity > 0) onQuantityChange(quantity - 1) }) {
            Icon(Icons.Filled.Remove, contentDescription = "Decrease quantity")
        }
        Text("$quantity", fontSize = 18.sp, modifier = Modifier.padding(horizontal = 16.dp))
        IconButton(onClick = { onQuantityChange(quantity + 1) }) {
            Icon(Icons.Filled.Add, contentDescription = "Increase quantity")
        }
    }
}
