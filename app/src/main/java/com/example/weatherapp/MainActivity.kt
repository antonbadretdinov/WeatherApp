package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.util.JsonToken
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MyLog","aaa");


        val jsonObject = JsonToken.valueOf("https://api.openweathermap.org/data/2.5/weather?lat=59.937500&lon=30.308611&appid=be59c27f4f9fe68c807fdd8034c9e97b&units=metric&lang=en");
    }
}
