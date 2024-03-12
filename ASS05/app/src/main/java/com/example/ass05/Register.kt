package com.example.ass05

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize

data class Register (
    var username: String,
    val pwd: String,
    val gender: String,
    val email: String,
    val selectedDate: Long,

    val formattedDate: String
): Parcelable

