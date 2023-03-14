package com.example.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.adapter.ForecastAdapter
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.helpers.MARK
import com.example.weatherapp.helpers.checkPermission
import com.example.weatherapp.model.CurrentWeather
import com.example.weatherapp.model.Data
import com.example.weatherapp.model.ForecastModel
import com.example.weatherapp.viewmodel.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (checkPermission(permissionName = Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(permissionName = Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                getLocation()
            }
        } else {
            requestLocation()
        }

        binding.recyclerForecast.layoutManager = LinearLayoutManager(this)

        viewModel.currentWeatherLiveData.observe(this) {
            showMainInfo(currentWeather = it)
        }
        viewModel.forecastLiveData.observe(this) {
            showWeatherOnRecycler(data = it.data)
        }
    }

    private fun showMainInfo(currentWeather: CurrentWeather) {
        binding.apply {
            currentWeather.data.forEach {
                tvCityName.text = it.city_name
                tvMainTemp.text = "${it.temp.toInt()} °C"
                tvWindSpeed.text = "Ветер: ${it.wind_spd.toInt()} м/c"
                tvFeelsLike.text = "Ощущается как: ${it.app_temp.toInt()} °C"
                tvDescription.text = it.weather.description
                imageMain.setImageDrawable(getMainBackground(it.weather.description))
            }
        }
    }

    private fun showWeatherOnRecycler(data: List<Data>) {
        binding.progressCircle.visibility = View.INVISIBLE
        val forecastModels: ArrayList<ForecastModel> = ArrayList()
        for (i in 0..6) {
            forecastModels.add(
                ForecastModel(
                    max_temp = data[i].max_temp,
                    min_temp = data[i].min_temp,
                    imageIcon = getForecastIcon(data[i].weather.description),
                    imageBackground = getForecastBackground(data[i].weather.description)
                )
            )
        }
        binding.recyclerForecast.adapter = ForecastAdapter(models = forecastModels)
    }

    private fun requestLocation() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            MARK
        )
        CoroutineScope(Dispatchers.IO).launch { getLocation() }
    }

    private fun getLocation() {
        val ct = CancellationTokenSource()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
        } else {
            fusedLocationProviderClient
                .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
                .addOnCompleteListener {
                    viewModel.getCurrentWeather(
                        lat = it.result.latitude,
                        lon = it.result.longitude
                    )
                    viewModel.getForecastWeather(
                        lat = it.result.latitude,
                        lon = it.result.longitude
                    )
                }
        }
    }

    private fun getMainBackground(description: String): Drawable? {
        return if (description.contains("Облачно") || description.contains("облачно"))
            ContextCompat.getDrawable(this, R.drawable.ic_main_cloud)
        else if (description.contains("Дождь") || description.contains("дождь"))
            ContextCompat.getDrawable(this, R.drawable.ic_main_rain)
        else if (description.contains("Снег") || description.contains("снег"))
            ContextCompat.getDrawable(this, R.drawable.ic_main_snow)
        else ContextCompat.getDrawable(this, R.drawable.ic_main_sun)
    }

    private fun getForecastBackground(description: String): Drawable? {
        return if (description.contains("Облачно") || description.contains("облачно"))
            ContextCompat.getDrawable(this, R.drawable.ic_forecast_cloud)
        else if (description.contains("Дождь") || description.contains("дождь"))
            ContextCompat.getDrawable(this, R.drawable.ic_forecast_cloud)
        else if (description.contains("Снег") || description.contains("снег"))
            ContextCompat.getDrawable(this, R.drawable.ic_forecast_snow)
        else ContextCompat.getDrawable(this, R.drawable.ic_forecast_sun)
    }

    private fun getForecastIcon(description: String): Drawable? {
        return if (description.contains("Облачно") || description.contains("облачно"))
            ContextCompat.getDrawable(this, R.drawable.ic_cloud_small_icon)
        else if (description.contains("Дождь") || description.contains("дождь"))
            ContextCompat.getDrawable(this, R.drawable.ic_rain_small_icon)
        else if (description.contains("Снег") || description.contains("снег"))
            ContextCompat.getDrawable(this, R.drawable.ic_snow_small_icon)
        else
            ContextCompat.getDrawable(this, R.drawable.ic_sun_small_icon)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.update) {
            CoroutineScope(Dispatchers.IO).launch {
                getLocation()
            }
            Toast.makeText(this, "Идет обновление", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}

