package com.example.lab2kotlin2.kotlin2

data class Subject(val id: String, val name: String, val credit: Int)

open class Person (val fName: String, val lName: String, val deptName: String){
    val firstName:String = fName.replaceFirstChar { it.uppercase() }
    val lastName:String = lName.replaceFirstChar { it.uppercase() }
    val department = "$deptName, College of Computing"

    open fun showDetail(){
        println("$firstName is at $department")
    }

    companion object{
        fun showCompanion(first_Name:String, last_Name:String, age:Int){
            println("Person is called from companion object : $first_Name $last_Name is $age years old.")
        }
    }
}

class Teacher(fName: String, lName: String, deptName: String, year:Int): Person(fName, lName, deptName){
    private var salary : Int = 0
    private var yearClass : Int = year
    private var creditClass : Int = 0

    override fun showDetail() {
        println("$firstName is a teacher for $yearClass years at $department.")
    }

    fun calSalary(){
        when{
            yearClass< 5 -> salary = 25000 + (2000 * yearClass)
            yearClass< 10 -> salary = 36000 + (2000 * (yearClass - 5))
            yearClass< 15 -> salary = 47000 + (2000 * (yearClass - 10))
            yearClass< 20 -> salary = 58000 + (2000 * (yearClass - 15))
            else -> salary = 60000 + (2000 * (yearClass - 20))
        }
        println("$firstName's salary is $salary baht")
    }

    fun teach(subj : Subject){
        println(subj.toString())
        creditClass += subj.credit
    }

    fun displayCredit(){
        println("$firstName teachers $creditClass credits.")
    }
}

object Singleton_Person{
    val first_Name = "David"
    val last_Name = "Bowie"
    var age = 23
    fun showCompanipn(){
        println("Person is called from singleton object : $first_Name $last_Name is $age years old.")

    }
}


class Student(fName: String, lName: String, deptName: String) : Person(fName, lName, deptName) {
    var creditTotal: Int = 0
    var gradeTotal: Double = 0.0

    override fun showDetail() {
        println("$firstName is a student at $department.")
    }

    fun gradeEnroll(subject: Subject, score: Int) {
        creditTotal += subject.credit

        var gpa : String =  when {
            score < 50 -> "F"
            score < 55 -> "D"
            score < 60 -> "D+"
            score < 65 -> "C"
            score < 70 -> "C+"
            score < 75 -> "B"
            score < 80 -> "B+"
            else -> "A"
        }

        gradeTotal += when{
            score <50 -> 0.0
            score <55 -> 1.0
            score <60 -> 1.5
            score <65 -> 2.0
            score <70 -> 2.5
            score <75 -> 3.0
            score <80 -> 3.5
            else -> 4.0
        } * subject.credit

        println("$subject Score: $score, Grade: $gpa")
    }

    fun displayGpa() {
        println("$firstName's GPA is %.2f".format(gradeTotal/creditTotal))
    }
}


fun main(){
//    var person1 = Person("Alice",  "Wonderland", "Computer Science")
//    println("Member NO 1 : " + person1.firstName + " " +person1.lastName)
//    person1.showDetail()
//    println()
//
//    println("Member NO 2 :")
//    Person.showCompanion("Bobby","Brown",25)
//    println()


    var subject1 = Subject("SC362007","Mobile Device Programming",3)
    var subject2 = Subject("SC362005","database Analysis and Design",3)
    var subject3 = Subject("SC361003","Object Oriented Concepts and Programming",1)

//    var person2 = Teacher("Chris","Evans","Infoemation Technology",25)
//    println("Member NO 3 : " + person2.firstName + " "+person2.lastName)
//    person2.showDetail()
//    person2.calSalary()
//    println(person2.firstName+" teaches: ")
//    person2.teach(subject1)
//    person2.teach(subject2)
//    person2.teach(subject3)
//    person2.displayCredit()
//    println()
//
//    println("Member NO 4 :")
//    Singleton_Person.showCompanipn()

    val person3 = Student("Grace", "Hopper", "Information Technology")
    println("Member NO 5 : ${person3.firstName} ${person3.lastName}")
    person3.showDetail()
    person3.gradeEnroll(subject1,65)
    person3.gradeEnroll(subject2,73)
    person3.gradeEnroll(subject3,90)
    person3.displayGpa()

}
