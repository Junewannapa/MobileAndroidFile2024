package com.example.mini_proj

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

interface FoodAPI {

    @GET("allFood")
    fun retriveFood(): Call<List<Food>>

    @FormUrlEncoded
    @POST("insert")
    fun insertFood(
        @Field("food_id") food_id: String,
        @Field("food_name") food_name: String,
        @Field("food_type") food_type: String,
        @Field("food_price") food_price: Int,
        @Field("food_quanti") food_quanti: Int,
        @Field("food_pic") food_pic: String
    ): Call<Food>
    @GET("allFood/{food_id}")
    fun retrievefood_id(
        @Path("food_id") food_id: Unit
    ): Call<Food>
    @FormUrlEncoded
    @PUT("updateFoodQuantity/{food_id}")
    fun updateFoodQuantity(
        @Path("food_id") food_id: String,
        @Field("food_quanti") food_quanti: Int
    ): Call<Unit>
    @DELETE("removeFoodFromCart/{food_id}")
    fun deleteFood(@Path("food_id") food_id: String): Call<Unit>


    @GET("allFoodType")
    fun retriveFoodType(): Call<List<FoodType>>

    companion object{
        fun create(): FoodAPI{
            val foodClient: FoodAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FoodAPI::class.java)
            return foodClient
        }
    }
}


