package com.pawlowski.sportnite.presentation.view_models_related.home_screen

import com.pawlowski.models.Meeting
import com.pawlowski.models.Sport
import com.pawlowski.models.User
import com.pawlowski.models.WeatherForecastDay
import com.pawlowski.utils.UiData

data class HomeScreenUiState(
    val user: User? = null,
    val weatherForecast: UiData<List<WeatherForecastDay>> = UiData.Loading(),
    val upcomingMeetings: UiData<List<Meeting>> = UiData.Loading(),
    val userSports: UiData<List<Sport>> = UiData.Loading()
)
