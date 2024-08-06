package com.sunnyweather.android.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.sunnyweather.android.R
import com.sunnyweather.android.databinding.ActivityWeatherBinding
import com.sunnyweather.android.databinding.ForecastBinding
import com.sunnyweather.android.databinding.ForecastItemBinding
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherActivity() : AppCompatActivity() {

    private val viewModel: WeatherViewModel by viewModels()

    private val binding by lazy { ActivityWeatherBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
    }

    private fun showWeatherInfo(weather: Weather) {
        binding.includedNow.placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily

// 填充now.xml布局中的数据
        val currentTempText = "${realtime.temperature.toInt()} ℃"
        binding.includedNow.currentTemp.text = currentTempText
        binding.includedNow.currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        binding.includedNow.currentAQI.text = currentPM25Text
        binding.includedNow.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)

// 填充forecast.xml布局中的数据
        binding.includedForecast.forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val viewBinding = ForecastItemBinding.inflate(LayoutInflater.from(this),
                binding.includedForecast.root,false)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            viewBinding.dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            viewBinding.skyIcon.setImageResource(sky.icon)
            viewBinding.skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            viewBinding.temperatureInfo.text = tempText
            binding.includedForecast.forecastLayout.addView(viewBinding.root)
        }

// 填充life_index.xml布局中的数据
        val lifeIndex = daily.lifeIndex
        binding.includedLifeIndex.coldRiskText.text = lifeIndex.coldRisk[0].desc
        binding.includedLifeIndex.dressingText.text = lifeIndex.dressing[0].desc
        binding.includedLifeIndex.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        binding.includedLifeIndex.carWashingText.text = lifeIndex.carWashing[0].desc
        binding.weatherLayout.visibility = View.VISIBLE
    }
}