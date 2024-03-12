package com.example.projectmobile

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuTypeClass(
    @Expose
    @SerializedName("food_type_id") val food_type_id: Int,

    @Expose
    @SerializedName("food_type_name") val food_type_name: String,

    @Expose
    @SerializedName("food_type_img") val food_type_img: String
): Parcelable