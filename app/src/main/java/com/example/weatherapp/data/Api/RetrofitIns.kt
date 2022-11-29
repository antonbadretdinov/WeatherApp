package com.example.weatherapp.data.Api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitIns {
    private val retrofit by lazy{
        Retrofit.Builder().baseUrl("https://api.weatherbit.io/").
        addConverterFactory(GsonConverterFactory.create()).build()
    }

val api: ApiService by lazy{
    retrofit.create(ApiService::class.java)
}
}
