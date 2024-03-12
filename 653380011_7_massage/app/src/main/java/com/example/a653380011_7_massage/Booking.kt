package com.example.a653380011_7_massage

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Booking(
    val name: String,
    val date: String,
    val room_type: String,
    val time: Int,
    val massage_type: String
): Parcelable