package com.example.mini_proj

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptScreen(navController: NavHostController) {
    val context = LocalContext.current
    var cartItems by remember { mutableStateOf(listOf<Food>()) }
    var subtotal by remember { mutableStateOf(0.0) }
    var vatAmount by remember { mutableStateOf(0.0) }
    var total by remember { mutableStateOf(0.0) }
    val vatRate = 7.0 // 7% VAT

    // Fetch the food items when the screen is composed
    LaunchedEffect(Unit) {
        FoodAPI.create().retrieveFood().enqueue(object : Callback<FoodResponse> {
            override fun onResponse(call: Call<FoodResponse>, response: Response<FoodResponse>) {
                if (response.isSuccessful) {
                    val foods = response.body()?.data?.filter { it.food_quanti > 0 } ?: emptyList()
                    subtotal = foods.sumOf { it.food_price.toDouble() * it.food_quanti }
                    vatAmount = subtotal * (vatRate / 100)
                    total = subtotal + vatAmount
                    cartItems = foods
                } else {
                    Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun submitOrder(foodItems: List<Food>, context: Context, api: FoodAPI) {
        // Use the provided FoodAPI instance to submit the orders
        foodItems.forEach { food ->
            val totalAmt = food.food_price * food.food_quanti
            api.submitOrder(
                food_id = food.food_id,
                food_quanti = food.food_quanti,
                total_amt = totalAmt
            ).enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Order for ${food.food_name} submitted successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to submit order for ${food.food_name}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "รายการอาหารทั้งหมด",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF57C00))
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display each food item in the bill
        LazyColumn {
            items(cartItems) { food ->
                BillItemRow(food = food)
            }
        }

        Divider(color = Color.Gray, thickness = 1.dp)

        // Display subtotal, VAT, and total
        SummaryRow(label = "ราคาอาหาร\t", amount = subtotal.format(2))
        SummaryRow(label = "VAT ($vatRate%)\t", amount = vatAmount.format(2))
        SummaryRow(label = "ราคารวมทั้งสิ้น\t", amount = total.format(2))
    }
    Button(
        onClick = { submitOrder(cartItems, context ,FoodAPI.create()) },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text("ส่งคำสั่งซื้อ", color = Color.White)
    }
}

@Composable
fun BillItemRow(food: Food) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = food.food_name)
        Text(text = "${food.food_price} บาท x ${food.food_quanti}")
    }
}

@Composable
fun SummaryRow(label: String, amount: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Text(text = amount)
    }
}

// The format extension function for Double remains the same as you defined.
fun Double.format(digits: Int) = "%.${digits}f บาท".format(this)

@Preview(showBackground = true)
@Composable
fun PreviewReceiptScreen() {
}
//ใส่เพิ่มในbuild.gradle.kts naka
//implementation ("androidx.compose.ui:ui:<version>")
//implementation ("androidx.compose.material:material:<version>")
//implementation ("androidx.compose.ui:ui-tooling-preview:<version>")