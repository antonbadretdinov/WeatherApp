package com.example.weatherapp.retrofit

import com.example.weatherapp.model.CurrentWeather
import com.example.weatherapp.model.Forecast
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {
    @GET("v2.0/forecast/daily?&units=M&lang=ru")
    suspend fun getForecastWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("key") key: String
    ): Forecast

    @GET("v2.0/current?&include=minutely&lang=ru")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("key") key: String
    ): CurrentWeather
}