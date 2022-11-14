package com.example.weatherapp.model

import android.graphics.drawable.Drawable
import java.util.*

class DailyForecastModel(
    val minTemp:Int? = 0,
    val maxTemp:Int? = 0,
    val image: Drawable? = null,
    val description: String? = null,
    var backgroundImage: Drawable? = null,
    var curDay : String = getCurrentDay())

private fun getCurrentDay(): String{
    val calendar = Calendar.getInstance()
    return when (calendar.get(Calendar.DAY_OF_WEEK)) {
        1 -> "Воскресенье"
        2 -> "Понедельник"
        3 -> "Вторник"
        4 -> "Среда"
        5 -> "Четверг"
        6 -> "Пятница"
        else -> {
            return "Суббота"
        }
    }
}