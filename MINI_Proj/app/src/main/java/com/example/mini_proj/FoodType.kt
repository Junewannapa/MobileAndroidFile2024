package com.example.mini_proj

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FoodType(
    @Expose
    @SerializedName("type_id") val type_id :Int,

    @Expose
    @SerializedName("type_name") val type_name :String,

    @Expose
    @SerializedName("type_img") val type_img :String
)
