package com.example.mini_proj

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FoodAPI {

    @GET("allFood")
    fun retrieveFood(): Call<FoodResponse>

    @FormUrlEncoded
    @POST("confirmOrder")
    fun confirmOrder(
        @Field("orderDetails") orderDetails: String // JSON string of order details or any suitable format
    ): Call<Unit>

    @FormUrlEncoded
    @POST("submitOrder")
    fun submitOrder(
        @Field("food_id") food_id: String,
        @Field("food_quanti") food_quanti: Int,
        @Field("total_amt") total_amt: Int
    ): Call<Unit>
    // Adjusted to match the server endpoint for adding a new food item.
    // Assuming your server expects these fields for a new food item.
    @FormUrlEncoded
    @POST("food") // Changed from "insert" to "food" to match server's POST endpoint
    fun insertFood(
        @Field("food_name") food_name: String,
        @Field("food_type_id") food_type_id: String, // Adjusted based on server's expected field
        @Field("price") price: Int,
        @Field("food_quantity") food_quantity: Int, // Adjusted based on server's expected field
        @Field("food_img") food_img: String
    ): Call<Food> // Assuming the server responds with the added food item details

    @GET("allFood/{food_id}")
    fun retrieveFoodById(
        @Path("food_id") food_id: String
    ): Call<Food>

    @FormUrlEncoded
    @PUT("updateFoodQuantity/{food_id}")
    fun updateFoodQuantity(
        @Path("food_id") foodId: String,
        @FieldMap(encoded = true) fields: Map<String, String>
    ): Call<Unit>

    @DELETE("removeFoodFromCart/{food_id}")
    fun deleteFood(
        @Path("food_id") food_id: String
    ): Call<Unit>

    companion object {
        fun create(): FoodAPI {
            return Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FoodAPI::class.java)
        }
    }
}