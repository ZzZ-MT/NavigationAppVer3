package com.example.navigationapp.repo.retrofit

import android.content.Context
import com.example.navigationapp.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    fun apiServices(context: Context): ApiServices {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(context.resources.getString(R.string.base_url))
            .build()

        return retrofit.create(ApiServices::class.java)
    }
}