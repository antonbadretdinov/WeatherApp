package com.example.weatherapp.model

import android.graphics.drawable.Drawable


class DailyForecastModel(
    val minTemp:Int? = 0,
    val maxTemp:Int? = 0,
    val image: Drawable? = null,
    val description: String? = null,
    var backgroundImage: Drawable? = null,
    var curDay: Int? = null)