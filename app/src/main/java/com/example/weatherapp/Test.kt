package com.example.weatherapp

import android.Manifest
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.*
import android.content.Context
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import com.google.android.gms.location.Priority


const val TOKEN="cf5ce9b04c663ea943cbe23110355483"
const val mark=1
var lat=0.0
var long=0.0


class Test: AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


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
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),
            mark
        )



        getLocation()

    }
//59.937500,30.308611
private fun getLocation(){
    val ct = CancellationTokenSource()
    fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        Log.d("mylog", "Connection denied")

        return
    } else {
        fusedLocationProviderClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
            .addOnCompleteListener { getData(it.result.latitude,it.result.longitude)}

    }
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
    private suspend fun getLocation2():Array<Double>{
        val ct = CancellationTokenSource()
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("mylog", "Connection denied")
        }
        long= fusedLocationProviderClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
            .result.longitude
        lat= fusedLocationProviderClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
            .result.latitude
        val gps = arrayOf(long,lat)
        return(gps)

    }
    suspend fun main()= coroutineScope {

        val job: Job = launch{
            delay(1000L)
            val gps:Array<Double> = getLocation2()
            long=gps[0]
            lat=gps[1]
        }
        job.start()

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
