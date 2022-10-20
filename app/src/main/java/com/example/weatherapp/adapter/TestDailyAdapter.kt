package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.model.DailyForecastModel

class TestDailyAdapter(private val forecastModel: Array<DailyForecastModel>):
    RecyclerView.Adapter<TestDailyAdapter.ViewHolder>() {

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tempAM : TextView = itemView.findViewById(R.id.dailyTempDay)
        val tempPM : TextView = itemView.findViewById(R.id.dailyTempEvening)
        val iconImage : ImageView = itemView.findViewById(R.id.dailyStateImage)
        val day : TextView = itemView.findViewById(R.id.tvDay)
        val backgroundImage : ImageView = itemView.findViewById(R.id.dailyForecastImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.daily_forecast_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tempAM.text = forecastModel[position].tempAM.toString()
        holder.tempPM.text = forecastModel[position].tempPM.toString()
        holder.iconImage.setImageDrawable(forecastModel[position].image)
        holder.backgroundImage.setImageDrawable(forecastModel[position].backgroundImage)
    }


    override fun getItemCount(): Int {
        return forecastModel.size
    }


}