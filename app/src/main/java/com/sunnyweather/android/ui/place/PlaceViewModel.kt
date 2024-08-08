package com.sunnyweather.android.ui.place

import Place
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.sunnyweather.android.logic.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.switchMap

class PlaceViewModel : ViewModel() {

    private val searchStateFlow = MutableStateFlow<String>("")

    val placeList = ArrayList<Place>()

    val placeStateFlow = searchStateFlow.filter { it.isNotBlank() }
        .flatMapLatest { query ->
            Repository.searchPlaces(query)
        }

    fun searchPlaces(query: String) {
        searchStateFlow.value = query
    }

    fun savePlace(place: Place) = Repository.savePlace(place)
    fun getSavedPlace() = Repository.getSavedPlace()
    fun isPlaceSaved() = Repository.isPlaceSaved()

}