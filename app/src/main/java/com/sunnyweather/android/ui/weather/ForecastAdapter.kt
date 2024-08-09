package com.sunnyweather.android.ui.weather

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R
import com.sunnyweather.android.databinding.ForecastBinding
import com.sunnyweather.android.databinding.ForecastItemBinding
import com.sunnyweather.android.logic.model.DailyResponse
import com.sunnyweather.android.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.Locale

class ForecastAdapter(private val context: Context, var daily: DailyResponse.Daily) :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ForecastItemBinding) : RecyclerView.ViewHolder(binding.root) {    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ForecastItemBinding.inflate(LayoutInflater.from(context), parent, false)
        val viewHolder = ViewHolder(binding)
        return viewHolder
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val skycon = daily.skycon[position]
        val temperature = daily.temperature[position]
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        binding.dateInfo.text = simpleDateFormat.format(skycon.date)
        val sky = getSky(skycon.value)
        binding.skyIcon.setImageResource(sky.icon)
        binding.skyInfo.text = sky.info
        val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
        binding.temperatureInfo.text = tempText
    }

    override fun getItemCount()= daily.skycon.size

}