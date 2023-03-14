package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ForecastItemBinding
import com.example.weatherapp.model.ForecastModel
import java.util.*

class ForecastAdapter(val models: List<ForecastModel>) :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ForecastItemBinding.bind(itemView)
        val day = binding.tvDay
        val background = binding.dailyForecastImage
        val icon = binding.dailyStateImage
        val maxTemp = binding.tvMaxTemp
        val minTemp = binding.tvMinTemp
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.forecast_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.day.text = if (holder.adapterPosition == 0) "Сегодня" else
            getCurrentDay(count = holder.adapterPosition)
        holder.maxTemp.text = "${models[position].max_temp.toInt()} °C"
        holder.minTemp.text = "${models[position].min_temp.toInt()} °C"
        holder.icon.setImageDrawable(models[position].imageIcon)
        holder.background.setImageDrawable(models[position].imageBackground)
    }

    private fun getCurrentDay(count: Int): String {
        val calendar = Calendar.getInstance()
        return run {
            val day = calendar.get(Calendar.DAY_OF_WEEK) + count
            if (day > 7) {
                getDay(day % 7)
            } else {
                getDay(day)
            }
        }
    }

    private fun getDay(dayCount: Int?): String {
        return when (dayCount) {
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
}