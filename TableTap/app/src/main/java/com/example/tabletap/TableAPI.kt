package com.example.tabletap

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
//    @GET("allTable")
//    fun retrieveTable(): Call<List<Table>>
//
//    @FormUrlEncoded
//    @POST("table")
//    fun insertTable(
//        @Field("tb_id") tb_id: String,
//        @Field("tb_name") tb_name: String,
//        @Field("status") status: String,
//        @Field("seat_count") seat_count: Int,
//        @Field("tb_pw") tb_pw: String
//    ): Call<Table>
//
//    @GET("tb/{tb_id}")
//    fun retrieveTableID(
//        @Path("tb_id") tb_id: String
//    ): Call<Table>
//
//    @FormUrlEncoded
//    @PUT("tb/{tb_id}")
//    fun updateTable(
//        @Path("tb_id") tb_id: String,
//        @Field("tb_name") tb_name: String,
//        @Field("status") status: String,
//        @Field("seat_count") seat_count: Int,
//        @Field("tb_pw") tb_pw: String
//    ): Call<Table>
//
//    @FormUrlEncoded
//    @PUT("tb/{tb_id}")
//    fun updateStatusTable(
//        @Path("tb_id") tb_id: String,
//        @Field("status") status: String
//    ): Call<Table>
//
////    @PUT("delete_soft/{tb_id}")
////    fun softDeleteTable(
////        @Path("tb_id") tb_id: String
////    ): Call<Table>
//
//    @DELETE("tb/{tb_id}")
//    fun deleteTable(
//        @Path("tb_id") tb_id: String
//    ): Call<Table>
@GET("allTable")
fun retrieveTable(): Call<List<Table>>

    @FormUrlEncoded
    @POST("table")
    fun insertTable(
        @Field("username") username: String,
        @Field("table_number") table_number: String,
        @Field("status") status: String,
        @Field("seat_count") seat_count: Int,
        @Field("password") password: String
    ): Call<Table>

    @GET("tb/{table_id}")
    fun retrieveTableID(
        @Path("table_id") table_id: String
    ): Call<Table>

    @FormUrlEncoded
    @PUT("tb/{table_id}")
    fun updateTable(
        @Path("table_id") table_id: String,
        @Field("username") username: String,
        @Field("table_number") table_number: String,
        @Field("status") status: String,
        @Field("seat_count") seat_count: Int,
        @Field("password") password: String
    ): Call<Table>

    @FormUrlEncoded
    @PUT("tb/{table_id}")
    fun updateStatusTable(
        @Path("table_id") table_id: String,
        @Field("status") status: String
    ): Call<Table>

//    @PUT("delete_soft/{tb_id}")
//    fun softDeleteTable(
//        @Path("tb_id") tb_id: String
//    ): Call<Table>

    @DELETE("tb/{table_id}")
    fun deleteTable(
        @Path("table_id") table_id: String,
    ): Call<Table>



    companion object {
        fun create(): TableAPI {
            val tbClient: TableAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TableAPI::class.java)
            return tbClient
        }
    }
}