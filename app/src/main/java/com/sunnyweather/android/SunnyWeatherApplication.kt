package com.sunnyweather.android

import android.app.Application
import android.content.Context

class SunnyWeatherApplication: Application() {

    companion object{
        lateinit var context: Context
        const val TOKEN = "cSoofFZNNqGygSol"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}