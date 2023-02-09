package com.pawlowski.sportnite.domain

import androidx.paging.PagingData
import com.pawlowski.models.*
import com.pawlowski.models.params_models.AddGameOfferParams
import com.pawlowski.models.params_models.OffersFilter
import com.pawlowski.models.params_models.PlayersFilter
import com.pawlowski.models.params_models.UserUpdateInfoParams
import com.pawlowski.sportnite.presentation.models.*
import com.pawlowski.utils.Resource
import com.pawlowski.utils.UiData
import kotlinx.coroutines.flow.Flow

interface IAppRepository {
    fun getWeatherForecast(): Flow<UiData<List<WeatherForecastDay>>>
    fun getUserSports(): Flow<UiData<List<Sport>>>

    fun getPagedNotifications(): Flow<PagingData<UserNotification>>

    fun getSportObjects(sportFilters: List<Sport>): Flow<UiData<List<SportObject>>>
    fun getUserNotifications(): Flow<UiData<List<UserNotification>>>
    fun getInfoAboutMe() : Flow<User?>




    suspend fun updateUserInfo(params: UserUpdateInfoParams): Resource<Unit>
    suspend fun updateAdvanceLevelInfo(levels: Map<Sport, AdvanceLevel>): Resource<Unit>

    fun signOut()
}