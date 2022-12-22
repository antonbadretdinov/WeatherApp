package com.example.weatherapp.data

import android.graphics.drawable.Drawable

data class CurrentWeatherData(
    val place: String,
    val description: String,
    val feels_like: Double,
    val windSpeed: Double,
    val currentTemp: Double,
    val hours: String,
)