package com.example.lab10_653380011_7

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginClass(
    @Expose
    @SerializedName("success") val success:Int,

    @Expose
    @SerializedName("std_id") val std_id:String
)
