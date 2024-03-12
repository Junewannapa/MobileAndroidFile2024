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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

object FoodStateHolder {
    val quantities = mutableStateMapOf<Int, Int>()
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController) {
    var textFieldID by remember { mutableStateOf("") }
    var textFieldName by remember { mutableStateOf("") }
    var textFieldprice by remember { mutableStateOf("") }

    val quantities = FoodStateHolder.quantities
    var textFieldordernumber by remember { mutableStateOf("") }
    var textFieldtype by remember { mutableStateOf("") }
    var foodItemsList = remember { mutableStateListOf<Food>() }
    val createClient = FoodAPI.create()
    val contextForToast = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var selectedFood by remember { mutableStateOf<Food?>(null) }
    var selectedCategory by remember { mutableStateOf("1") } // Holds the name of the category // Start with the first category by default
    var foodList = remember { mutableStateListOf<Food>() }
    var foodTypeItemsList = remember { mutableStateListOf<FoodType>() }
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
        createClient.retriveFood().enqueue(object : Callback<List<Food>> {
            override fun onResponse(call: Call<List<Food>>, response: Response<List<Food>>) {
                if (response.isSuccessful) {
                    foodList.addAll(response.body()?.filter { it.food_type == selectedCategory }
                        ?: emptyList())
                }
            }

            override fun onFailure(call: Call<List<Food>>, t: Throwable) {
                Toast.makeText(contextForToast, "Error on Failure" + t.message, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    fun updateQuantity(foodId: Int, newQuantity: Int) {
        if (newQuantity >= 0) {
            // Assuming that your API expects a String for the foodId
            createClient.updateFoodQuantity(foodId.toString(), newQuantity)
                .enqueue(object : Callback<Unit> {
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        if (response.isSuccessful) {
                            quantities[foodId] = newQuantity
                        } else {
                            Toast.makeText(contextForToast, "Update failed", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        Toast.makeText(contextForToast, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    fun updateCategoryAndFilterList(categoryId: String) {
        selectedCategory = categoryId
        createClient.retriveFood()
            .enqueue(object : Callback<List<Food>> {
                override fun onResponse(
                    call: Call<List<Food>>,
                    response: Response<List<Food>>
                ) {
                    response.body()?.let { foods ->
                        foodList.clear()
                        foodList.addAll(foods.filter { it.food_type == selectedCategory })
                    }
                }

                override fun onFailure(call: Call<List<Food>>, t: Throwable) {
                    Toast.makeText(
                        contextForToast, "Error on Failure: " + t.message,
                        Toast.LENGTH_LONG
                    ).show()
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
//            LazyRow(
//                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
//                horizontalArrangement = Arrangement.spacedBy(space = 20.dp),
//                contentPadding = PaddingValues(all = 10.dp),
//
//                ) {
//                items(categories) { category ->
//                    CategoryItem(category = category) {
//                        selectedCategory = category.id
//                        // Handle category selection
//                        // For example, filter the foodList based on the category selected
//                    }
//                }
//            }
            LazyRow(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 20.dp),
                contentPadding = PaddingValues(all = 10.dp),
            ) {
                items(foodTypeItemsList) { foodType ->
                    FoodTypeItem(foodType = foodType) {
                        selectedCategory = foodType.type_id.toString()
                        updateCategoryAndFilterList(foodType.type_id.toString())
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
                                        updateQuantity(item.food_id.toInt(), newQuantity)
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
                                    updateQuantity(item.food_id.toInt(), newQuantity)
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

@Composable
fun FoodTypeItem(foodType: FoodType, onFoodTypeClick: (FoodType) -> Unit) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .clickable { onFoodTypeClick(foodType) }
            .size(
                width = 70.dp,
                height = 70.dp
            ), // Set a fixed size for each food type item
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Load the image from foodType (replace R.drawable.placeholder)
            Image(
                painter = rememberAsyncImagePainter(data = foodType.type_img),
                contentDescription = foodType.type_name,
                modifier = Modifier.size(20.dp)
            )
            Text(text = foodType.type_name, textAlign = TextAlign.Center)
        }
    }
}


