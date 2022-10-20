package com.example.weatherapp

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.adapter.TestDailyAdapter
import com.example.weatherapp.adapter.TestHourlyAdapter
import com.example.weatherapp.model.DailyForecastModel
import com.example.weatherapp.model.HourlyForecastModel
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

    private var icon1 : Drawable? = null
    private var icon2 : Drawable? = null
    private var icon3 : Drawable? = null
    private var icon4 : Drawable? = null


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_layout)


        val backDailyRain : Drawable? = ContextCompat.getDrawable(this,R.drawable.ic_forecast_rain)
        val backDailySun : Drawable? = ContextCompat.getDrawable(this,R.drawable.ic_forecast_sun)
        val backDailyCloud : Drawable? = ContextCompat.getDrawable(this,R.drawable.ic_forecast_cloud)
        val backDailySnow : Drawable? = ContextCompat.getDrawable(this,R.drawable.ic_forecast_snow)

        icon1 = ContextCompat.getDrawable(this,R.drawable.ic_cloud_small_icon)
        icon2 = ContextCompat.getDrawable(this,R.drawable.ic_rain_small_icon)
        icon3 = ContextCompat.getDrawable(this,R.drawable.ic_snow_small_icon)
        icon4 = ContextCompat.getDrawable(this,R.drawable.ic_sun_small_icon)
        val icon5 = icon1
        val icon6 = icon2
        val icon7 = icon3
        val icon8 = icon4

        val model1H = HourlyForecastModel(21,icon1)//hourlyForecastModels
        val model2H = HourlyForecastModel(14,icon2)
        val model3H = HourlyForecastModel(7,icon3)
        val model4H = HourlyForecastModel(4,icon4)
        val model5H = HourlyForecastModel(24,icon5)
        val model6H = HourlyForecastModel(13,icon6)
        val model7H = HourlyForecastModel(27,icon7)
        val model8H = HourlyForecastModel(31,icon8)

        val model1D = DailyForecastModel(21,12,icon1,backDailyCloud)//dailyForecastModels
        val model2D = DailyForecastModel(12,-22,icon2,backDailyRain)
        val model3D = DailyForecastModel(21,3,icon3,backDailySnow)
        val model4D = DailyForecastModel(3,-3,icon4,backDailySun)



        val hourlyForecast : Array<HourlyForecastModel> = arrayOf(model1H,model2H,model3H,model4H,model5H,model6H,model7H,model8H)
        val recyclerViewHourly : RecyclerView = findViewById(R.id.recyclerHourlyForecast)
        recyclerViewHourly.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        recyclerViewHourly.adapter = TestHourlyAdapter(hourlyForecast)

        val dailyForecast : Array<DailyForecastModel> = arrayOf(model1D,model2D,model3D,model4D)
        val recyclerViewDaily : RecyclerView = findViewById(R.id.recyclerDailyForecast)
        recyclerViewDaily.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        recyclerViewDaily.adapter = TestDailyAdapter(dailyForecast)


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
