package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.helpers.KEY
import com.example.weatherapp.model.CurrentWeather
import com.example.weatherapp.model.Forecast
import com.example.weatherapp.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val mutableCurrentWeatherLiveData = MutableLiveData<CurrentWeather>()
    val currentWeatherLiveData: MutableLiveData<CurrentWeather> = mutableCurrentWeatherLiveData

    private val mutableForecastLiveData = MutableLiveData<Forecast>()
    val forecastLiveData: LiveData<Forecast> = mutableForecastLiveData

    private val weatherApi = RetrofitInstance.service

    fun getCurrentWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            val currentWeather = withContext(Dispatchers.IO) {
                weatherApi.getCurrentWeather(
                    lat = lat,
                    lon = lon,
                    key = KEY
                )
            }
            mutableCurrentWeatherLiveData.value = currentWeather
        }
    }

    fun getForecastWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            val forecast = withContext(Dispatchers.IO) {
                weatherApi.getForecastWeather(
                    lat = lat,
                    lon = lon,
                    key = KEY
                )
            }
            mutableForecastLiveData.value = forecast
        }
    }
}