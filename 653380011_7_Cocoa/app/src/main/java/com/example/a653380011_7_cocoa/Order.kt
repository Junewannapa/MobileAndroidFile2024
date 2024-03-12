package com.example.a653380011_7_cocoa

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Order(
    @Expose
    @SerializedName("id") val id:Int,

    @Expose
    @SerializedName("customer") val customer: String,

    @Expose
    @SerializedName("glass_size") val glass_size: String,

    @Expose
    @SerializedName("number_of_glass") val number_of_glass: Int,

    @Expose
    @SerializedName("sweet") val sweet: Int,

    @Expose
    @SerializedName("price") val price: Int
)
