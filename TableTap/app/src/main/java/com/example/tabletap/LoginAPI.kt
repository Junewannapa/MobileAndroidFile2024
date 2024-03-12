package com.example.tabletap

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface LoginAPI {

    @GET("allRegister")
    fun retrieveMember(): Call<List<LoginClass>>


    @GET("/login/{username}/{password}")
    fun login(
        @Path("username") username: String,
        @Path("password") password: String,
    ): Call<LoginClass>


    @FormUrlEncoded
    @POST("insertAccount")
    fun registerUser(
        @Field("username") username: String,
        @Field("password") password: String,
    ): Call<LoginClass>


    companion object {
        fun create(): LoginAPI {
            val tbClient: LoginAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LoginAPI::class.java)
            return tbClient
        }
    }
}
