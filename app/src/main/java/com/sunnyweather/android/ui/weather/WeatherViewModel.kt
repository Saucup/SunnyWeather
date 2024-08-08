package com.sunnyweather.android.ui.weather

import Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.sunnyweather.android.logic.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.switchMap

class WeatherViewModel : ViewModel() {

    private val locationStateFlow = MutableStateFlow<Location?>(null)

    var locationLng = ""
    var locationLat = ""
    var placeName = ""

    val weatherFlow = locationStateFlow
        .filterNotNull()
        .flatMapLatest { location ->
            Repository.refreshWeather(location.lng, location.lat)
        }

    fun refreshWeather(lng: String, lat: String) {
        locationStateFlow.value = Location(lng, lat)
    }

}