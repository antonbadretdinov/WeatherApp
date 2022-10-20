package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.model.HourlyForecastModel

class TestHourlyAdapter(private val forecastModel: Array<HourlyForecastModel>):
    RecyclerView.Adapter<TestHourlyAdapter.ViewHolder>() {

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val temp : TextView = itemView.findViewById(R.id.tvHourlyTemp)
        val iconImage : ImageView = itemView.findViewById(R.id.imageHourlyState)
        val hour : TextView = itemView.findViewById(R.id.tvHour)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.hourly_forecast_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.temp.text = forecastModel[position].temp.toString()
        holder.iconImage.setImageDrawable(forecastModel[position].image)
    }

    override fun getItemCount(): Int {
        return forecastModel.size
    }
}