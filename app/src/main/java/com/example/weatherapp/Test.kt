package com.example.weatherapp

import kotlinx.coroutines.*
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.adapter.TestDailyAdapter
import com.example.weatherapp.adapter.TestHourlyAdapter
import com.example.weatherapp.data.Api.ApiService
import com.example.weatherapp.model.DailyForecastModel
import com.example.weatherapp.model.HourlyForecastModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.lang.Thread.sleep


const val TOKEN="318125c470f64fb3829482ebd1518bd9"
const val mark=1


class Test: AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    private var currentData: TestData? = null
    private var dailyForecastArray: ArrayList<DailyForecastModel>? = null
    private var mainBackground: ImageView? = null
    private var hourlyImageView: ImageView? = null

    private var name: TextView? = null
    private var mainTemp: TextView? = null
    private var description: TextView? = null
    private var feelsLike: TextView? = null
    private var windSpeed: TextView? = null

    private var icon1: Drawable? = null
    private var icon2: Drawable? = null
    private var icon3: Drawable? = null
    private var icon4: Drawable? = null
    private var backDailyRain: Drawable? = null
    private var backDailySun: Drawable? = null
    private var backDailyCloud: Drawable? = null
    private var backDailySnow: Drawable? = null

    var appScope = CoroutineScope(Dispatchers.IO)

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_layout)

        icon1 = ContextCompat.getDrawable(this, R.drawable.ic_cloud_small_icon)
        icon2 = ContextCompat.getDrawable(this, R.drawable.ic_rain_small_icon)
        icon3 = ContextCompat.getDrawable(this, R.drawable.ic_snow_small_icon)
        icon4 = ContextCompat.getDrawable(this, R.drawable.ic_sun_small_icon)

        val iconD1 = ContextCompat.getDrawable(this, R.drawable.ic_forecast_cloud)
        val iconD2 = ContextCompat.getDrawable(this, R.drawable.ic_forecast_snow)
        val iconD3 = ContextCompat.getDrawable(this, R.drawable.ic_forecast_sun)
        val iconD4 = ContextCompat.getDrawable(this, R.drawable.ic_forecast_rain)

        val model1H = HourlyForecastModel(12, icon4)
        val model2H = HourlyForecastModel(32, icon2)
        val model3H = HourlyForecastModel(22, icon1)
        val model4H = HourlyForecastModel(2, icon3)
        val model5H = HourlyForecastModel(-22, icon2)
        val model6H = HourlyForecastModel(12, icon1)
        val model7H = HourlyForecastModel(32, icon3)
        val model8H = HourlyForecastModel(-4, icon4);
        val hourlyForecast: Array<HourlyForecastModel> =
            arrayOf(model1H, model2H, model3H, model4H, model5H, model6H, model7H, model8H)
        val recyclerViewHourly: RecyclerView = findViewById(R.id.recyclerHourlyForecast)
        recyclerViewHourly.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recyclerViewHourly.adapter = TestHourlyAdapter(hourlyForecast)


        val model1D = DailyForecastModel(0, 3, icon4, "", iconD1)
        val model2D = DailyForecastModel(12, 22, icon1, "", iconD2)
        val model3D = DailyForecastModel(-2, 21, icon2, "", iconD3)
        val model4D = DailyForecastModel(2, 12, icon3, "", iconD4)
        val model5D = DailyForecastModel(1, 3, icon4, "", iconD1)


        val dailyForecast: Array<DailyForecastModel> =
            arrayOf(model1D, model2D, model3D, model4D, model5D)
        val recyclerViewDaily: RecyclerView = findViewById(R.id.recyclerDailyForecast)
        recyclerViewDaily.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerViewDaily.adapter = TestDailyAdapter(dailyForecast)


        mainBackground = findViewById(R.id.imageMain)
        mainBackground?.setImageResource(R.drawable.ic_main_loading)
        hourlyImageView = findViewById(R.id.imageHourlyForecast)

        name = findViewById(R.id.tvCityName)
        mainTemp = findViewById(R.id.tvMainTemp)
        description = findViewById(R.id.tvMainState)
        feelsLike = findViewById(R.id.tvFeelsLike)
        windSpeed = findViewById(R.id.tvWindSpeed)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            mark
        )



        Log.d("mylog", "Test0")
        val job: Job=appScope.launch{
              getLocation()
        }

        Log.d("mylog","Test2")
}



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

        Log.d("mylog","Test1")
    }



    private  fun getData(lat:Double,lon:Double){

        val URL="https://api.weatherbit.io/v2.0/current?" +
                "lat=$lat" +
                "&lon=$lon" +
                "&key=$TOKEN"+
                "&units=M&lang=ru"
        val URLForecast7="https://api.weatherbit.io/v2.0/forecast/daily?" +
                "lat=$lat" +
                "&lon=$lon" +
                "&key=$TOKEN"+
                "&units=M&lang=ru"


        val queue= Volley.newRequestQueue(this)
        val jobOfrequest1: Job=appScope.launch{


            val request= StringRequest(
                Request.Method.GET,URL,
                {
                        result -> parseData(result)
                },
                {error->Log.d("mylog","Result: $error")}
            )
            queue.add(request)
            Log.d("mylog","Test3")
        }

        val jobOfrequest2: Job=appScope.launch{

            val request2= StringRequest(
                Request.Method.GET,URLForecast7,
                {
                        result2 -> parseDataFromDailyForecast(result2)
                },
                {error->Log.d("mylog","Result: $error")}
            )
            queue.add(request2)
            Log.d("mylog","Test4")
        }

    }


    private fun parseDataFromDailyForecast(result: String){
        val mainJSONObject=JSONObject(result)
        for (i in 0..6)
        {
        val value1 = DailyForecastModel(
            mainJSONObject.getJSONArray("data")
                .getJSONObject(i).getDouble("min_temp").toInt(),

            mainJSONObject.getJSONArray("data")
                .getJSONObject(i).getDouble("max_temp").toInt(),

            if(mainJSONObject.getJSONArray("data")
                    .getJSONObject(i).getJSONObject("weather").getString("description").contains("Облачно"))icon1
            else if(mainJSONObject.getJSONArray("data")
                    .getJSONObject(i).getJSONObject("weather").getString("description").contains("Дождь"))icon2
            else if(mainJSONObject.getJSONArray("data")
                    .getJSONObject(i).getJSONObject("weather").getString("description").contains("Снег"))icon3
            else icon4,

            mainJSONObject.getJSONArray("data")
                .getJSONObject(i).getJSONObject("weather").getString("description"),

            if(mainJSONObject.getJSONArray("data").
                getJSONObject(i).getJSONObject("weather").getString("description").contains("Облачно")) backDailyCloud
            else if(mainJSONObject.getJSONArray("data").
                getJSONObject(i).getJSONObject("weather").getString("description").contains("Дождь")) backDailyRain
            else if(mainJSONObject.getJSONArray("data").
                getJSONObject(i).getJSONObject("weather").getString("description").contains("Снег")) backDailySnow
            else backDailySun
        )
            dailyForecastArray?.add(value1)
            Log.d("mylog",value1.description.toString()+","+dailyForecastArray?.get(i)?.minTemp.toString()+","+value1.maxTemp.toString())
        }






    }

    private fun parseData(result: String){
        val mainJSONObject=JSONObject(result)
        currentData= TestData(
            mainJSONObject.getJSONArray("data").getJSONObject(0).getString("city_name"),
            mainJSONObject.getJSONArray("data").getJSONObject(0).getJSONObject("weather").getString("description"),
            mainJSONObject.getJSONArray("data").getJSONObject(0).getDouble("app_temp"),
            mainJSONObject.getJSONArray("data").getJSONObject(0).getDouble("wind_spd"),
            mainJSONObject.getJSONArray("data").getJSONObject(0).getDouble("temp"),
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
        windSpeed?.text = "Скорость ветра: "+ data.windSpeed.toInt()+" км/ч"

         if(data.description.contains("Облачно")) {
                mainBackground?.setImageResource(R.drawable.ic_main_sun)
                hourlyImageView?.setImageResource(R.drawable.ic_hourly_back_sun)
            }else if(data.description.contains("Дождь")){
                mainBackground?.setImageResource(R.drawable.ic_main_rain)
                hourlyImageView?.setImageResource(R.drawable.ic_hourly_back_rain)
            }else if(data.description.contains("Снег")) {
                mainBackground?.setImageResource(R.drawable.ic_main_snow)
                hourlyImageView?.setImageResource(R.drawable.ic_hourly_forecast_snow)
            } else{
                mainBackground?.setImageResource(R.drawable.ic_main_cloud)
                hourlyImageView?.setImageResource(R.drawable.ic_hourly_back_cloud)
            }
        }
    }


interface ApiService {
    @GET("v2.0/current?lat={lat}&lon={lon}&key=$TOKEN&units=M&lang=ru")
    suspend fun getCourseData(): Response<HourlyForecastModel>


    @GET("v2.0/forecast/daily?lat={lat}&lon={lon}&key=$TOKEN&units=M&lang=ru")
    suspend fun getDailyData(): Response<DailyForecastModel>
}

object RetrofitIns {
    private val retrofit by lazy{
        Retrofit.Builder().baseUrl("https://api.weatherbit.io/").
        addConverterFactory(GsonConverterFactory.create()).build()
    }

    val api: ApiService by lazy{
        retrofit.create(ApiService::class.java)
    }
}







