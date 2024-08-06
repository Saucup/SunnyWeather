package com.sunnyweather.android.ui.place

import Place
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R
import com.sunnyweather.android.databinding.PlaceItemBinding
import com.sunnyweather.android.ui.weather.WeatherActivity

class PlaceAdapter(private val fragment: Fragment, private val placeList: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    inner class ViewHolder(val itemBinding: PlaceItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        val placeName: TextView = itemBinding.placeName
        val placeAddress: TextView = itemBinding.placeAddress
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceAdapter.ViewHolder {
        val itemBinding = PlaceItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val holder = ViewHolder(itemBinding)
        itemBinding.root.setOnClickListener{
            val position = holder.bindingAdapterPosition
            val place = placeList[position]
            val intent = Intent(parent.context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            fragment.startActivity(intent)
//            fragment.activity?.finish()
        }
        return holder
    }

    override fun onBindViewHolder(holder: PlaceAdapter.ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }

    override fun getItemCount() = placeList.size

}