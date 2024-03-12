package com.example.tabletap

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Table(
    @Expose
    @SerializedName("table_id") val table_id: String,
    @Expose
    @SerializedName("username") val username: String,
    @Expose
    @SerializedName("table_number") val table_number: String,
    @Expose
    @SerializedName("status") val status: String,
    @Expose
    @SerializedName("seat_count") val seat_count: Int,
    @Expose
    @SerializedName("password") val password: String

): Parcelable
