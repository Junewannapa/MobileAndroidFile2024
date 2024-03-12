package com.example.lab05

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Student (
    var id: String,
    val name: String,
    val age: Int,
    val hobby: List<String>
): Parcelable