package com.example.lab1kotlin1.kotlin1

fun main() {
    val subjectScore = arrayOf(67, 52, 73, 85, 42, 78)
    println("There are " + subjectScore.size + " subjects in array.")
    calculateGrade(subjectScore)
}

fun calculateGrade(scoreArr: Array<Int>){
    var i: Int = 1
    var grade: String
    for (value in scoreArr){
        grade = when{
            value <50 -> "F"
            value <55 -> "D"
            value <60 -> "D+"
            value <65 -> "C"
            value <70 -> "C+"
            value <75 -> "B"
            value <80 -> "B+"
            else -> "A"
        }

        println("Grade of Subject Number $i : $value = $grade")
        i++
    }
}