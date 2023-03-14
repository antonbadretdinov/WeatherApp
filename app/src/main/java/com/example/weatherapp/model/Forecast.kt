package com.example.weatherapp.model

data class Forecast(
    val data: List<Data>
)

data class Data(
    val weather: WeatherData,
    val temp: Double,
    val max_temp: Double,
    val min_temp: Double,
)

data class WeatherData(
    val description: String
)