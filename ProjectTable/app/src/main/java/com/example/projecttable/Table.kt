package com.example.projecttable

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Table(
    @Expose
    @SerializedName("td_id") val td_id: String,
    @Expose
    @SerializedName("td_name") val td_name: String,
    @Expose
    @SerializedName("status") val status: String,
    @Expose
    @SerializedName("seat_count") val seat_count: Int,
    @Expose
    @SerializedName("tb_pw") val tb_pw: String
):Parcelable
