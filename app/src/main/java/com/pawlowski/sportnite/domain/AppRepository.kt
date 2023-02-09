package com.pawlowski.sportnite.domain

import com.pawlowski.models.Sport
import com.pawlowski.models.WeatherForecastDay
import com.pawlowski.sportnite.presentation.models.SportObject
import com.pawlowski.utils.UiData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(

) : IAppRepository {

    override fun getWeatherForecast(): Flow<UiData<List<WeatherForecastDay>>> {
        TODO("Not yet implemented")
    }

    override fun getSportObjects(sportFilters: List<Sport>): Flow<UiData<List<SportObject>>> {
        TODO("Not yet implemented")
    }

}



