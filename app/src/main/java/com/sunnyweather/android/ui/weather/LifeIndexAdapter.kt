package com.sunnyweather.android.ui.weather

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R
import com.sunnyweather.android.databinding.ForecastBinding
import com.sunnyweather.android.databinding.ForecastItemBinding
import com.sunnyweather.android.databinding.LifeIndexBinding
import com.sunnyweather.android.databinding.LifeIndexItemBinding
import com.sunnyweather.android.logic.model.DailyResponse
import com.sunnyweather.android.logic.model.getLifeIndex
import com.sunnyweather.android.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.Locale

class LifeIndexAdapter(private val context: Context, var lifeDescriptions: List<Pair<String,DailyResponse.LifeDescription>>) :
    RecyclerView.Adapter<LifeIndexAdapter.ViewHolder>() {

        private val TAG = "view tag lifeIndex"

    inner class ViewHolder(val binding: LifeIndexItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LifeIndexItemBinding.inflate(LayoutInflater.from(context), parent, false)
        val viewHolder = ViewHolder(binding)
        return viewHolder
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val (info, description) = lifeDescriptions[position]
        Log.d(TAG, "onBindViewHolder: $info")
        val lifeIndex = getLifeIndex(info)

        binding.lifeIndexImg.setImageResource(lifeIndex.icon)
        binding.lifeIndexImg.visibility = View.VISIBLE
        binding.lifeIndexInfo.text=lifeIndex.name
        binding.lifeIndexText.text=description.desc

    }

    override fun getItemCount()= lifeDescriptions.size

}