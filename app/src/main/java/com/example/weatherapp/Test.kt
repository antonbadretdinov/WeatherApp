package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import org.w3c.dom.Text
import java.util.logging.Level.parse


const val TOKEN="cf5ce9b04c663ea943cbe23110355483"


class Test: AppCompatActivity() {
    private var currentData: TestData? = null

    private var name : TextView? = null
    private var mainTemp : TextView? = null
    private var description : TextView? = null
    private var feelsLike : TextView? = null
    private var windSpeed : TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_layout)
        name = findViewById(R.id.tvCityName)
        mainTemp = findViewById(R.id.tvMainTemp)
        description = findViewById(R.id.tvMainState)
        feelsLike = findViewById(R.id.tvFeelsLike)
        windSpeed = findViewById(R.id.tvWindSpeed)
        getData(59.937500,30.308611)
    }


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
            ""
        )
        putDataToLayout(currentData!!)
    }

    @SuppressLint("SetTextI18n")
    private fun putDataToLayout(data:TestData){
        name?.text = data.place
        mainTemp?.text = data.currentTemp.toInt().toString()+"°C"
        description?.text = data.description
        feelsLike?.text = "Ощущается как: "+ data.feels_like.toInt().toString()+"°C"
        windSpeed?.text = "Скорость ветра: "+ data.windSpeed+" км/ч"


    }
}
