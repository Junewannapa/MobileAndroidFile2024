package com.example.a653380011_7_cocoa

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

interface OrderAPI {
    @GET("allOrder")
    fun retrieveOrder(): Call<List<Order>>

    @FormUrlEncoded
    @POST("order")
    fun insertOrder(
        @Field("customer") customer: String,
        @Field("glass_size") glass_size: String,
        @Field("number_of_glass") number_of_glass: Int,
        @Field("sweet") sweet: Int,
        @Field("price") price: Int
    ): Call<Order>


    companion object {
        fun create(): OrderAPI {
            val orderClient: OrderAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrderAPI::class.java)
            return orderClient
        }
    }
}