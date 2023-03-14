package com.example.weatherapp.model

import android.graphics.drawable.Drawable

data class ForecastModel(
    val max_temp: Double,
    val min_temp: Double,
    val imageIcon: Drawable?,
    val imageBackground: Drawable?
)