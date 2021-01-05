package com.estimote.indoorapp

import com.estimote.indoorsdk_module.cloud.Location
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path


interface FindItLocations {
    @Headers("Context:application/json",
            "Authorization: Basic ZmluZGl0LWVzcTplYjgwZWQ4MDUxZTY3YTkyNzFiNzYwNDk0ZjdlZWFhNQ==")
    @GET("locations/{identifier}/")
    fun getSingleLocation(@Path ("identifier") locationId:String): Call<LocationPin>
}