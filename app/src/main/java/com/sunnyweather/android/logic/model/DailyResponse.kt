package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class DailyResponse(val status: String, val result: Result) {

    companion object{
        fun emptyDaily() = Daily(
            emptyList(), emptyList(), emptyLifeIndex()
        )

        fun emptyLifeIndex() = emptyMap<String,List<LifeDescription>>()
    }

    data class Result(val daily: Daily)
    data class Daily(
        val temperature: List<Temperature>, val skycon: List<Skycon>,
        @SerializedName("life_index") val lifeIndex: Map<String,List<LifeDescription>>
    )

    data class Temperature(val max: Float, val min: Float)
    data class Skycon(val value: String, val date: Date)


    data class LifeDescription(val desc: String)
}