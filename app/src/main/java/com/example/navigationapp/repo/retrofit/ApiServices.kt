package com.example.navigationapp.repo.retrofit

import com.example.navigationapp.model.directions.DirectionResponses
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("maps/api/directions/json")
    fun getDirection(@Query("origin") origin: String,
                     @Query("destination") destination: String,
                     @Query("key") apiKey: String): Call<DirectionResponses>

//    @GET("/maps/api/place/autocomplete/json")
//    fun getPlaces(@Query())
}