package com.example.pjtabletap

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MenuTypeAPI {
    @GET("allmenutype")
    fun retrieveMenuType(): Call<List<MenuTypeClass>> // แก้ตรงนี้เป็น List<String> เพื่อเก็บค่า type_name เท่านั้น

    @Multipart
    @POST("inserttype")
    fun inserttype(
        @Part image: MultipartBody.Part,
        @Part("food_type_name") food_type_name : RequestBody,
    ): Call<MenuTypeClass>

    companion object{
        fun create(): MenuTypeAPI {
            val menutypeClient: MenuTypeAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:4000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MenuTypeAPI::class.java)
            return menutypeClient
        }
    }
}
