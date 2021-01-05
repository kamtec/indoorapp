package com.estimote.indoorapp


import retrofit2.Call
import retrofit2.http.*

interface FindItUsers{
    @Headers("Context:application/json")
    @POST("authenticate")
    fun login(@Query("username") mail: String, @Query("password") password: String): Call <User>


  @Headers("Context:application/json")
    @POST("user/create")
    fun insert(@Body  user:User): Call <User>

    @Headers("Context:application/json")
    @POST("user/update")
    fun updatepaasword(@Body userdto: UserDTO): Call <UserDTO>





}