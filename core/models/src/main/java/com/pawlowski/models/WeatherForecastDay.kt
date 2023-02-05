package com.pawlowski.models

import com.pawlowski.utils.UiDate

data class WeatherForecastDay(
    val weatherIcon: String,
    val temperature: Int,
    val date: UiDate
)