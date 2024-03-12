package com.example.pjtabletap

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuClass(
    @Expose
    @SerializedName("food_id") val food_id: Int,

    @Expose
    @SerializedName("food_name") val food_name: String,

    @Expose
    @SerializedName("price") val price: Int,

    @Expose
    @SerializedName("food_img") val food_img: String,

    @Expose
    @SerializedName("food_quantity") val food_quantity: Int = 0,

    @Expose
    @SerializedName("food_type_id") val food_type_id: Int // เพิ่มฟิลด์ food_type_id ที่เป็น foreign key
): Parcelable