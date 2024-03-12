package com.example.mini_proj

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Food(
    @Expose
    @SerializedName("food_id") val food_id :String,

    @Expose
    @SerializedName("food_name") val food_name :String,

    @Expose
    @SerializedName("food_type") val food_type :String,

    @Expose
    @SerializedName("food_quanti") val food_quanti :Int,

    @Expose
    @SerializedName("food_price") val food_price :Int,

    @Expose
    @SerializedName("food_pic") val food_pic :String


    )

