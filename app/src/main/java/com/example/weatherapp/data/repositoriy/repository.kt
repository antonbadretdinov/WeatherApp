package com.example.weatherapp.data.repositoriy

import com.example.weatherapp.data.Api.RetrofitIns
import com.example.weatherapp.model.DailyForecastModel
import com.example.weatherapp.model.HourlyForecastModel
import retrofit2.Response

class repository
{  suspend fun getCourseData(): Response<HourlyForecastModel>{
    return RetrofitIns.api.getCourseData()

}
    suspend fun getDailyData(): Response<DailyForecastModel>{
        return RetrofitIns.api.getDailyData()
    }
}