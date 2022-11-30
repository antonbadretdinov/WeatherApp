package com.example.weatherapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.model.DailyForecastModel
import java.util.*

class TestDailyAdapter(private val forecastModel: ArrayList<DailyForecastModel>?):
    RecyclerView.Adapter<TestDailyAdapter.ViewHolder>() {

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tempMin : TextView = itemView.findViewById(R.id.dailyTempDay)
        val tempMax : TextView = itemView.findViewById(R.id.dailyTempEvening)
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
        if(forecastModel?.get(position)?.curDay==0){
            holder.day.text = "Сегодня"
        }else{
            holder.day.text=getCurrentDay(forecastModel?.get(position)?.curDay)
        }
        holder.tempMin.text = forecastModel?.get(position)?.minTemp.toString()
        holder.tempMax.text = forecastModel?.get(position)?.maxTemp.toString()
        holder.iconImage.setImageDrawable(forecastModel?.get(position)?.image)
        holder.backgroundImage.setImageDrawable(forecastModel?.get(position)?.backgroundImage)
    }

    private fun getCurrentDay(count:Int? = null): String {
        val calendar = Calendar.getInstance()
        return if(count!=null){
            val day = calendar.get(Calendar.DAY_OF_WEEK) + count
            if(day>7){
                getDay(day%7)
            }else {
                getDay(day)
            }
        }else {
            getDay(calendar.get(Calendar.DAY_OF_WEEK))
        }
    }
    private fun getDay(dayCount: Int?): String{
        return when(dayCount){
            1 -> "Воскресенье"
            2 -> "Понедельник"
            3 -> "Вторник"
            4 -> "Среда"
            5 -> "Четверг"
            6 -> "Пятница"
            else -> {
                return "Суббота"
            }
        }
    }

    override fun getItemCount(): Int {
            return forecastModel!!.size
    }
}