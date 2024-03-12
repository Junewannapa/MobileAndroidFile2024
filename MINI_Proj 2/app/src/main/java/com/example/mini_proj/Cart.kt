package com.example.mini_proj

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ShoppingCartScreen(
    navController: NavHostController // Assuming you've set up the Retrofit instance
) {
    val context = LocalContext.current
    val createClient = FoodAPI.create()
    var foodItems by remember { mutableStateOf<List<Food>>(emptyList()) }
    var total by remember { mutableStateOf(0) }

    // Fetch cart items when ShoppingCartScreen appears
    LaunchedEffect(Unit) {
        createClient.retrieveFood()
            .enqueue(object : Callback<FoodResponse> { // Changed to FoodResponse
                override fun onResponse(
                    call: Call<FoodResponse>,
                    response: Response<FoodResponse>
                ) {
                    if (response.isSuccessful) {
                        // Adjusted for the FoodResponse class
                        val filteredFoodItems =
                            response.body()?.data?.filter { it.food_quanti > 0 } ?: emptyList()
                        foodItems = filteredFoodItems
                        total = filteredFoodItems.sumOf { it.food_price * it.food_quanti }
                    }
                }

                override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
                    // Handle failure
                }
            })
    }
    // Function to update quantity in the backend when + or - is pressed
    // Assuming updateFoodQuantity expects a String for the ID and an Int for the quantity.
    // Inside ShoppingCartScreen
    fun updateQuantity(foodId: String, newQuantity: Int) {
        val safeNewQuantity = maxOf(newQuantity, 0) // Ensure quantity is not negative

        // Attempt to convert the foodId to an integer
        val foodIdInt = foodId.toIntOrNull()
        if (foodIdInt != null) {
            // Create a map with the updated quantity
            val requestBody = mapOf("food_quantity" to safeNewQuantity.toString())

            // Perform the Retrofit call to update the food quantity
            createClient.updateFoodQuantity(foodId, requestBody).enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        // Update the UI to reflect the new quantity
                        foodItems = foodItems.map { food ->
                            if (food.food_id == foodId) food.copy(food_quanti = safeNewQuantity) else food
                        }
                        // Recalculate the total
                        total = foodItems.sumOf { it.food_price * it.food_quanti }
                    } else {
                        // Handle unsuccessful update
                        Toast.makeText(context, "Failed to update quantity", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    // Handle failure to communicate with the server
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            // Handle the error case where foodId is not an integer
            Toast.makeText(context, "Invalid food ID format", Toast.LENGTH_SHORT).show()
        }
    }
//    fun confirmOrder(foodItems: List<Food>, context: Context) {
//        val gson = Gson()
//        val orderDetailsJson = gson.toJson(foodItems.map { it.toOrderDetail() })
//        createClient.confirmOrder(orderDetailsJson).enqueue(object : Callback<Unit> {
//            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
//                if (response.isSuccessful) {
//                    Toast.makeText(context, "Order confirmed", Toast.LENGTH_LONG).show()
//                    // Optionally, clear the cart or navigate to another screen
//                } else {
//                        // Handle error
//                    }
//                }
//            override fun onFailure(call: Call<Unit>, t: Throwable) {
//                    // Handle failure to communicate with the server
//                }
//            })
//    }


    fun deleteFoodItem(food: Food) {
        createClient.deleteFood(food.food_id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    // Remove the food item from the UI list
                    foodItems = foodItems.filterNot { it.food_id == food.food_id }
                    total = foodItems.sumOf { it.food_price * it.food_quanti }
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                // Handle failure, perhaps show a toast
            }
        })
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(foodItems) { food ->
                FoodItemRow(
                    food = food,
                    onQuantityChange = { updatedFood, newQuantity ->
                        updateQuantity(updatedFood.food_id, newQuantity)
                    },
                    onDelete = { foodToDelete ->
                        deleteFoodItem(foodToDelete)
                    }
                )
            }
        }
        Text(
            text = "ราคารวม $total บาท",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(8.dp)
        )
        Button(
            onClick = {
                navController.navigate(Screen.Bill.route)
            },
            colors = ButtonDefaults.buttonColors(contentColor = Color(0xFF4CAF50)),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "สั่งอาหาร",
                color = Color.White,
                fontSize = 20.sp
            )
        }
//        Button(
//            onClick = {
//                confirmOrder(foodItems, context) // Call the function to confirm the order
//                navController.navigate(Screen.Bill.route)
//            })
//    }
    }
}


@Composable
fun FoodItemRow(
    food: Food,
    onQuantityChange: (Food, Int) -> Unit,
    onDelete: (Food) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(10.dp),
        // Add some elevation to the card for depth
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(
                    data = food.food_pic,
                    builder = { crossfade(true) }
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp)) // Add space between the image and the text
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = food.food_name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "${food.food_price} บาท",
                    color = Color.Gray, // Use a more muted color for the price text
                    fontSize = 14.sp
                )
            }
            IconButton(
                onClick = {
                    val newQuantity = if (food.food_quanti > 1) food.food_quanti - 1 else 0
                    onQuantityChange(food, newQuantity) // This calls the function passed into FoodItemRow
                },
                modifier = Modifier
                    .background(color = Color(0xFF4F9752), CircleShape)
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Remove",
                    tint = Color.White
                )
            }
            Text(
                text = food.food_quanti.toString(),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 8.dp),
                fontSize = 18.sp
            )
            IconButton(
                onClick = { onQuantityChange(food, food.food_quanti + 1) },
                modifier = Modifier
                    .background(color = Color(0xFF4F9752), CircleShape)
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { onDelete(food) },
                modifier = Modifier
                    .background(Color.Red, CircleShape)
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        }
    }
}