package com.pawlowski.sportnite.presentation.view_models_related.home_screen

import com.pawlowski.sportnite.presentation.models.Meeting
import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.sportnite.presentation.models.User
import com.pawlowski.sportnite.presentation.models.WeatherForecastDay
import com.pawlowski.utils.UiData

data class HomeScreenUiState(
    val user: User? = null,
    val weatherForecast: UiData<List<WeatherForecastDay>> = UiData.Loading(),
    val upcomingMeetings: UiData<List<Meeting>> = UiData.Loading(),
    val userSports: UiData<List<Sport>> = UiData.Loading()
)
