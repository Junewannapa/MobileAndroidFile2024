package com.example.lab11sql

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student(val id: String,
                   val name: String,
                   val gender: String,
                   val age: Int
): Parcelable {
}

