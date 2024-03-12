package com.example.ass08mysql_queryinsert

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EmployeeAPI {

    @GET("allemp")
    fun retriveEmployee(): Call<List<Employee>>

    @FormUrlEncoded
    @POST("emp")
    fun insertEmp(
        @Field("emp_name") emp_name: String,
        @Field("emp_gender") emp_gender: String,
        @Field("emp_email") emp_email: String,
        @Field("emp_salary") emp_salary: Int):Call<Employee>


    @FormUrlEncoded
    @PUT("emp/{emp_id}")
    fun updateEmployee(
        @Path("emp_id") emp_id: String,
        @Field("emp_name") emp_name: String,
        @Field("emp_gender") emp_gender: String,
        @Field("emp_email") emp_email: String,
        @Field("emp_salary") emp_salary: Int
    ): Call<Employee>

    @DELETE("emp/{emp_id}")
    fun deleteEmployee(
        @Path("emp_id") emp_id: String
    ): Call<Employee>

    companion object{
        fun create(): EmployeeAPI{
            val empClient: EmployeeAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(EmployeeAPI::class.java)
            return empClient
        }
    }
}