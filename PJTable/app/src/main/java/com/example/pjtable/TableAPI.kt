package com.example.pjtable

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

interface TableAPI {
    @GET("allTable")
    fun retrieveTable(): Call<List<Table>>

    @FormUrlEncoded
    @POST("table")
    fun insertTable(
        @Path("td_id") td_id: String,
        @Field("tb_name") tb_name: String,
        @Field("status") status: String,
        @Field("seat_count") seat_count: Int,
        @Field("tb_pw") tb_pw: String
    ): Call<Table>

    @GET("table/{td_id}")
    fun retrieveTableID(
        @Path("td_id") td_id: String
    ): Call<Table>

    @FormUrlEncoded
    @PUT("td/{td_id}")
    fun updateTable(
        @Field("td_id") td_id: String,
        @Field("td_name") td_name: String,
        @Field("status") status: String,
        @Field("seat_count") seat_count: Int,
        @Field("tb_pw") tb_pw: String
    ): Call<Table>

    @PUT("delete_soft/{std_id}")
    fun softDeleteStudent(
        @Path("std_id") td_id: String
    ): Call<Table>

    @DELETE("td/{td_id}")
    fun deleteTable(
        @Path("td_id") td_id: String
    ): Call<Table>


    companion object {
        fun create(): TableAPI {
            val tdClient: TableAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:9990/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TableAPI::class.java)
            return tdClient
        }
    }
}