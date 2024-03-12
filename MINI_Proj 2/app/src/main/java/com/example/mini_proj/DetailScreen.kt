package com.example.mini_proj

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

object FoodStateHolder {
    val quantities = mutableStateMapOf<Int, Int>()
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController) {
    val quantities = FoodStateHolder.quantities
    val createClient = FoodAPI.create()
    val contextForToast = LocalContext.current
    var selectedCategory by remember { mutableStateOf("1") } // Holds the name of the category // Start with the first category by default
    var foodList = remember { mutableStateListOf<Food>() }
    val backgroundColorInt = 0xFFEC7777.toInt()
    val categories = listOf(
        Category(
            "1",
            "ซูซิ/ซาซิมิ",
            R.drawable.salmon_nigiri
        ), // Replace with your actual drawable resources
        Category("2", "ของทานเล่น", R.drawable.placeholder_pizza),
        Category("3", "ของหวาน", R.drawable.placeholder_pizza),
        Category("4", "เครื่องดื่ม", R.drawable.placeholder_pizza),
    )
    foodList.clear()
    LaunchedEffect(selectedCategory) {
        createClient.retrieveFood().enqueue(object : Callback<FoodResponse> {
            override fun onResponse(
                call: Call<FoodResponse>,
                response: Response<FoodResponse>
            ) {
                if (response.isSuccessful) {
                    // Use the 'data' property of FoodResponse to get the list of Food
                    // Correct property name as per your data class
                    foodList.addAll(response.body()?.data?.filter { it.food_type == selectedCategory }
                        ?: emptyList())
                }
            }

            override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
                Toast.makeText(contextForToast, "Error on Failure: " + t.message, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    fun updateQuantity(foodId: String, newQuantity: Int) {
        val safeNewQuantity = maxOf(newQuantity, 0) // Ensure quantity is not negative

        // Create a request body map with the updated quantity as a string
        val requestBody = mapOf("food_quantity" to safeNewQuantity.toString())
        if (safeNewQuantity >= 0) {
            createClient.updateFoodQuantity(foodId, requestBody).enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                            // Create a copy of the map with the updated quantity
                            val updatedQuantities = quantities.toMutableMap().apply {
                                put(foodId.toInt(), newQuantity)
                            }
                            // Update the state map in a thread-safe way
                            quantities.clear()
                            quantities.putAll(updatedQuantities)
                        } else {
                            Toast.makeText(contextForToast, "Update failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        Toast.makeText(contextForToast, "Error: ${t.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
        }
    }

    fun updateCategoryAndFilterList(categoryId: String) {
        selectedCategory = categoryId
        createClient.retrieveFood()
            .enqueue(object : Callback<FoodResponse> { // Change List<Food> to FoodResponse
                override fun onResponse(
                    call: Call<FoodResponse>, // Change List<Food> to FoodResponse
                    response: Response<FoodResponse> // Change List<Food> to FoodResponse
                ) {
                    if (response.isSuccessful) {
                        // Clear the current food list
                        foodList.clear()
                        // Add all the foods from the response that match the selected category
                        foodList.addAll(response.body()?.data?.filter { it.food_type == selectedCategory } ?: emptyList())
                    }
                }

                override fun onFailure(call: Call<FoodResponse>, t: Throwable) { // Change List<Food> to FoodResponse
                    Toast.makeText(contextForToast, "Error on Failure: " + t.message, Toast.LENGTH_LONG).show()
                }
            })
    }


    @Composable
    fun Topbar(title: String, background: Int) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Adjust the height to match the second image
                .background(color = Color(background))
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(
                        start = 16.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    ), // Adjust padding as necessary
                color = Color.White,
                fontSize = 18.sp, // Adjust font size as necessary
                fontWeight = FontWeight.Bold
            )
        }
    }

        Column (

                    modifier = Modifier.fillMaxSize()

        ){
            Topbar(title = "เมนู", background = backgroundColorInt)
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
            }
            // LazyRow for categories
            LazyRow(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 20.dp),
                contentPadding = PaddingValues(all = 10.dp),

                ) {
                items(categories) { category ->
                    CategoryItem(category = category) {
                        selectedCategory = category.id
                        // Handle category selection
                        // For example, filter the foodList based on the category selected
                    }
                }
            }
            //แบบนี้ส่งยังมี ค่าอยู่
//            LazyColumn(
//                modifier = Modifier.fillMaxSize(),
//                contentPadding = PaddingValues(all = 10.dp),
//                verticalArrangement = Arrangement.spacedBy(space = 5.dp),
//            ) {
//            items(foodList.filter { it.food_type == selectedCategory }) { item ->
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .wrapContentHeight()
//                        .padding(horizontal = 5.dp, vertical = 5.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = Color.White,
//                    ),
//                    elevation = CardDefaults.cardElevation(
//                        defaultElevation = 2.dp
//                    ),
//                    shape = RoundedCornerShape(corner = CornerSize(16.dp))
//                ) {
//                    Column(
//                        Modifier
//                            .padding(5.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Image(
//                            painter = rememberAsyncImagePainter(item.food_pic),
//                            contentDescription = item.food_name,
//                            modifier = Modifier
//                                .height(150.dp)
//                                .width(150.dp)
//                        )
//                        Text(
//                            text = "${item.food_name}\t\t\t${item.food_price}.-",
//                            textAlign = TextAlign.Center
//                        )
//                        // Quantity row with + and - buttons
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(8.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            IconButton(onClick = {
//                                val newQuantity = (quantities[item.food_id.toInt()] ?: 0) - 1
//                                if (newQuantity >= 0) {
//                                    updateQuantity(item.food_id.toInt(), newQuantity)
//                                }
//                            }) {
//                                Icon(Icons.Default.Remove, contentDescription = "Decrease")
//                            }
//                            Text(
//                                text = (quantities[item.food_id.toInt()] ?: 0).toString(),
//                                textAlign = TextAlign.Center
//                            )
//                            IconButton(onClick = {
//                                val newQuantity = (quantities[item.food_id.toInt()] ?: 0) + 1
//                                updateQuantity(item.food_id.toInt(), newQuantity)
//                            }) {
//                                Icon(Icons.Default.Add, contentDescription = "Increase")
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        }
//}
// อันนี้จะทำให้ค่ามันรีกลับเป็น 0
            LazyVerticalGrid(

                modifier = Modifier.weight(1f),
                columns = GridCells.Adaptive(minSize = 130.dp),
                verticalArrangement = Arrangement.spacedBy(space = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 5.dp),
                contentPadding = PaddingValues(all = 40.dp)
            ) {
                itemsIndexed(
                    items = foodList.filter { it.food_type == selectedCategory },
                ) { index, item ->
                    Card(
                        modifier = Modifier
                            .width(240.dp)
                            .wrapContentHeight()
                            .padding(horizontal = 5.dp, vertical = 5.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        ),
                        shape = RoundedCornerShape(corner = CornerSize(16.dp))
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
                            // Quantity row with + and - buttons
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(onClick = {
                                    val newQuantity = (quantities[item.food_id.toInt()] ?: 0) - 1
                                    if (newQuantity >= 0) {
                                        updateQuantity(item.food_id, newQuantity)
                                    }
                                }) {
                                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
                                }
                                Text(
                                    text = (quantities[item.food_id.toInt()] ?: 0).toString(),
                                    textAlign = TextAlign.Center
                                )
                                IconButton(onClick = {
                                    val newQuantity = (quantities[item.food_id.toInt()] ?: 0) + 1
                                    updateQuantity(item.food_id, newQuantity)
                                }) {
                                    Icon(Icons.Default.Add, contentDescription = "Increase")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

@Composable
fun CategoryItem(category: Category, onCategoryClick: (Category) -> Unit) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable { onCategoryClick(category) }
                        .size(
                            width = 70.dp,
                            height = 70.dp
                        ), // Set a fixed size for each category item
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = category.imageResId),
                            contentDescription = category.name,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(text = category.name, textAlign = TextAlign.Center)
                    }
                }
}


