package com.example.lab1kotlin1.kotlin1


fun main() {
    val subjectScore = arrayOf(48, 65, 71, 81, 56)
    println("There are " + subjectScore.size + " subjects in array.")
    CalculateGPAX(subjectScore)
}

fun CalculateGPAX(scoreArr: Array<Int>){
    var i: Int = 1
    var GPAX = 0.0
    for (value in scoreArr) {
        val grade = when {
            value < 50 -> "F"
            value < 55 -> "D"
            value < 60 -> "D+"
            value < 65 -> "C"
            value < 70 -> "C+"
            value < 75 -> "B"
            value < 80 -> "B+"
            else -> "A"
        }
        val GPAXPoint = when (grade) {
            "A" -> 4.0
            "B+" -> 3.5
            "B" -> 3.0
            "C+" -> 2.5
            "C" -> 2.0
            "D+" -> 1.5
            "D" -> 1.0
            else -> 0.0
        }
        println("Grade of Subject Number $i : $value = $grade : $GPAXPoint")
        GPAX += GPAXPoint
        i++
    }
    val totalGPAX = GPAX / scoreArr.size
    println("GPAX = ((0.0*3) + (2.5*3) + (3.0*3) + (4.0*3) + (1.5*3)) / 15 = $totalGPAX")
}
