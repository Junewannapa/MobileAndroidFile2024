package com.example.mini_proj

import com.google.gson.annotations.SerializedName

data class Food(
    @SerializedName("food_id") val food_id: String,
    @SerializedName("food_name") val food_name: String,
    @SerializedName("food_type_id") val food_type: String,
    @SerializedName("price") val food_price: Int,
    @SerializedName("food_img") val food_pic: String,
    @SerializedName("food_quantity") val food_quanti: Int // If quantity is a string in JSON
    // Other properties...
)
data class FoodResponse(
    @SerializedName("data") val data: List<Food>
)
