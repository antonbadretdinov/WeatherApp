package com.example.weatherapp.data.Api

import com.example.weatherapp.TOKEN
import com.example.weatherapp.model.DailyForecastModel
import com.example.weatherapp.model.HourlyForecastModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("v2.0/current?lat=30.00&lon=30.00&key=$TOKEN&units=M&lang=ru")
    suspend fun getCourseData(): Response<HourlyForecastModel>


    @GET("v2.0/forecast/daily?lat={lat}&lon={lon}&key=$TOKEN&units=M&lang=ru")
    suspend fun getDailyData(): Response<DailyForecastModel>
}

class repository
{  suspend fun getCourseData(): Response<HourlyForecastModel>{
    return RetrofitIns.api.getCourseData()

}
    suspend fun getDailyData(): Response<DailyForecastModel>{
        return RetrofitIns.api.getDailyData()
    }
}