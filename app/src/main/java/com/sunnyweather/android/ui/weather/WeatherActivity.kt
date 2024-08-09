package com.sunnyweather.android.ui.weather

import android.annotation.SuppressLint
import android.app.ProgressDialog.show
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunnyweather.android.R
import com.sunnyweather.android.databinding.ActivityWeatherBinding
import com.sunnyweather.android.databinding.ForecastBinding
import com.sunnyweather.android.databinding.ForecastItemBinding
import com.sunnyweather.android.logic.model.DailyResponse
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.model.getSky
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherActivity() : AppCompatActivity() {

    val viewModel: WeatherViewModel by viewModels()

    val binding by lazy { ActivityWeatherBinding.inflate(layoutInflater) }

    val forecastAdapter by lazy{ForecastAdapter(this, DailyResponse.emptyDaily())}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.getInsetsController(window, window.decorView).apply {

            isAppearanceLightStatusBars = true
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        window.statusBarColor = Color.TRANSPARENT
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

        binding.swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.teal_200))
        refreshWeather()
        binding.swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }

        binding.includedForecast.forecastLayout.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.includedForecast.forecastLayout.adapter = forecastAdapter

        binding.includedNow.navBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {}
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE)
                        as InputMethodManager
                manager.hideSoftInputFromWindow(
                    drawerView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        })

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.weatherFlow.collect { result ->
                    val weather = result.getOrNull()
                    if (weather != null) {
                        showWeatherInfo(weather)
                    } else {
                        Toast.makeText(
                            this@WeatherActivity,
                            "无法成功获取天气信息",
                            Toast.LENGTH_SHORT
                        ).show()
                        result.exceptionOrNull()?.printStackTrace()
                    }
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }

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

//填充forecast.xml布局中的数据
        forecastAdapter.daily = weather.daily
        forecastAdapter.notifyDataSetChanged()

// 填充life_index.xml布局中的数据
        val lifeIndex = daily.lifeIndex

        binding.includedLifeIndex.coldRiskText.text = lifeIndex.coldRisk[0].desc
        binding.includedLifeIndex.dressingText.text = lifeIndex.dressing[0].desc
        binding.includedLifeIndex.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        binding.includedLifeIndex.carWashingText.text = lifeIndex.carWashing[0].desc

        binding.weatherLayout.visibility = View.VISIBLE
    }

    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        binding.swipeRefresh.isRefreshing = false
    }

}