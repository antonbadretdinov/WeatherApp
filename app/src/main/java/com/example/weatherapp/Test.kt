package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


const val TOKEN="cf5ce9b04c663ea943cbe23110355483"


class Test: AppCompatActivity() {
    private var currentData: TestData? = null

    private var mainBackground : ImageView? = null
    private var hourlyImageView : ImageView? = null

    private var name : TextView? = null
    private var mainTemp : TextView? = null
    private var description : TextView? = null
    private var feelsLike : TextView? = null
    private var windSpeed : TextView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_layout)


        val list = intArrayOf(1,1,1,1,1,1,1,1,1).toCollection(ArrayList())

        val recyclerView : RecyclerView = findViewById(R.id.recyclerHourlyForecast)
        recyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        recyclerView.adapter = TestAdapter(list)

        mainBackground = findViewById(R.id.imageMain)
        hourlyImageView = findViewById(R.id.imageHourlyForecast)

        name = findViewById(R.id.tvCityName)
        mainTemp = findViewById(R.id.tvMainTemp)
        description = findViewById(R.id.tvMainState)
        feelsLike = findViewById(R.id.tvFeelsLike)
        windSpeed = findViewById(R.id.tvWindSpeed)
        getData(32.21,10.308611)
    }
//59.937500,30.308611

    private fun getData(lat:Double,lon:Double){
        val URL="https://api.openweathermap.org/data/2.5/weather?" +
                "lat=$lat" +
                "&lon=$lon" +
                "&appid=$TOKEN"+
                "&units=metric&lang=ru"
        val queue= Volley.newRequestQueue(applicationContext)
        val request= StringRequest(
            Request.Method.GET,URL,
            {
                result -> parseData(result)
            },
            {error->Log.d("mylog","Result: $error")}
        )
        queue.add(request)
    }

    private fun parseData(result: String){
        val mainJSONObject=JSONObject(result)
        currentData= TestData(
            mainJSONObject.getString("name"),
            mainJSONObject.getJSONArray("weather").getJSONObject(0).getString("description"),
            mainJSONObject.getJSONObject("main").getDouble("feels_like"),
            mainJSONObject.getJSONObject("wind").getString("speed"),
            mainJSONObject.getJSONObject("main").getDouble("temp"),
            mainJSONObject.getJSONArray("weather").getJSONObject(0).getString("main"),
            ""
        )
        putDataToLayout(currentData!!)
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    private fun putDataToLayout(data:TestData){
        name?.text = data.place
        mainTemp?.text = data.currentTemp.toInt().toString()+"°C"
        description?.text = data.description
        feelsLike?.text = "Ощущается как: "+ data.feels_like.toInt()+" °C"
        windSpeed?.text = "Скорость ветра: "+ data.windSpeed[0]+" км/ч"

        when (data.mainDesc) {
            "Clear" -> {
                mainBackground?.setImageResource(R.drawable.ic_main_sun)
                hourlyImageView?.setImageResource(R.drawable.ic_hourly_back_sun)
            }
            "Rain" -> {
                mainBackground?.setImageResource(R.drawable.ic_main_rain)
                hourlyImageView?.setImageResource(R.drawable.ic_hourly_back_rain)
            }
            "Snow" -> {
                mainBackground?.setImageResource(R.drawable.ic_main_snow)
                hourlyImageView?.setImageResource(R.drawable.ic_hourly_forecast_snow)
            }
            else -> {
                mainBackground?.setImageResource(R.drawable.ic_main_cloud)
                hourlyImageView?.setImageResource(R.drawable.ic_hourly_back_cloud)
            }
        }
    }
}

/*
*переменная облачность
* небольшая облачность
* пасмурно
* ясно
* облачно с прояснениями
* небольшой дождь
*
* Clouds
* Rain
* Clear
* Snow
* */
