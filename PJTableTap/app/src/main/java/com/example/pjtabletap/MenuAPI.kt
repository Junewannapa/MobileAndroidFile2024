package com.example.pjtabletap

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface MenuAPI {
    @GET("allmenu")
    fun retrieveMenu(): Call<List<MenuClass>>

    @FormUrlEncoded
    @POST("insert")
    fun insertMenu(
        @Field("food_name") food_name: String,
        @Field("price") price: Int,
        @Field("food_img") food_img: String
    ): Call<MenuClass>

    @Multipart
    @POST("insertUpload")
    fun uploadMenu(
        @Part image: MultipartBody.Part,
        @Part("food_name") foodName: RequestBody,
        @Part("price") price: RequestBody,
        @Part("food_type_id") foodTypeId: RequestBody // เพิ่มพารามิเตอร์สำหรับ food_type_id
    ): Call<MenuClass>


    @FormUrlEncoded
    @PUT("update/{food_id}")
    fun updateMenu(
        @Path("food_id") food_id: Int,
        @Field("food_name") food_name: String,
        @Field("price") price: Int,
        @Field("food_img") food_img: String,
        @Field("food_type_id") food_type_id: Int // เพิ่มพารามิเตอร์สำหรับ food_type_id
    ): Call<MenuClass>


    @Multipart
    @PUT("editUpload/{food_id}")
    fun editImage(
        @Path("food_id") food_id: Int, // เปลี่ยนจาก id เป็น menu_id
        @Part("food_name") food_name: RequestBody,
        @Part("price") price: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<MenuClass>


    @DELETE("delete/{food_id}")
    fun deleteMenu(
        @Path("food_id") food_id: Int
    ): Call<MenuClass>

    companion object {
        fun create(): MenuAPI {
            val menuClient: MenuAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:4000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MenuAPI::class.java)
            return menuClient
        }
    }
}