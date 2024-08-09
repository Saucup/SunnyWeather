package com.sunnyweather.android.logic.model

import com.sunnyweather.android.R

class LifeIndex(val info:String, val name:String, val icon:Int) {}

private val lifeIndex = mapOf(
    "coldRisk" to LifeIndex("coldRisk", "感冒", R.drawable.ic_coldrisk),
    "ultraviolet" to LifeIndex("ultraviolet", "实时紫外线", R.drawable.ic_ultraviolet),
    "carWashing" to LifeIndex("carWashing", "洗车", R.drawable.ic_carwashing),
    "dressing" to LifeIndex("dressing", "穿衣", R.drawable.ic_dressing),
)

fun getLifeIndex(info: String): LifeIndex  {
    return lifeIndex[info] ?: LifeIndex(info, info , R.drawable.ic_default)
}