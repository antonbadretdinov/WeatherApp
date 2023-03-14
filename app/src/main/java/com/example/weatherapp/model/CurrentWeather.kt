package com.example.weatherapp.model

data class CurrentWeather(
    val data: List<DataCurrent>
)

data class DataCurrent(
    val city_name: String,
    val temp: Double,
    val wind_spd: Double,
    val app_temp: Double,
    val weather: Weather
)

data class Weather(
    val description: String
)