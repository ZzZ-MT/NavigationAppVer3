package com.example.navigationapp.repo.retrofit

import com.example.navigationapp.model.DirectionResponses
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("maps/api/directions/json")
    fun getDirection(@Query("origin") origin: String,
                     @Query("destination") destination: String,
                     @Query("key") apiKey: String): Call<DirectionResponses>
}