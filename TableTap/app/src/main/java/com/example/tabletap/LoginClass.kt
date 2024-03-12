package com.example.tabletap

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginClass(
    @Expose
    @SerializedName("success") val success: Int,
    @Expose
    @SerializedName("username") val username: String,
    @Expose
    @SerializedName("role") val role: String

)
