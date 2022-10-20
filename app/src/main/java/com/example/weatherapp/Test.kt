package com.example.weatherapp

import android.Manifest
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.*
import android.content.Context
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import org.json.JSONObject
import com.google.android.gms.location.Priority


const val TOKEN="cf5ce9b04c663ea943cbe23110355483"
const val mark=1
var lat=0.0
var long=0.0


class Test: AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    private var currentData: TestData? = null
    private var coord: CoordData? = null

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
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),
            mark
        )



        getLocation()

    }


    /*override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            mark -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Log.d("mylog", "Connection denied")
                }
                return
            }
        }
    }*/
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
        val queue= Volley.newRequestQueue(this)
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




private fun parseCurrentData(mainJSONObject: JSONObject){
    currentData= TestData(
        mainJSONObject.getString("timezone"),
        mainJSONObject.getJSONArray("weather").getJSONObject(0).getString("description"),
        mainJSONObject.getJSONObject("main").getDouble("feels_like"),
        mainJSONObject.getJSONObject("wind").getString("speed"),
        mainJSONObject.getJSONObject("main").getDouble("temp"),
        mainJSONObject.getJSONArray("weather").getJSONObject(0).getString("main"),
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



    private suspend fun getLocation2():Array<Double>{
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





