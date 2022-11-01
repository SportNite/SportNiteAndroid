package com.pawlowski.sportnite.domain

import com.pawlowski.sportnite.domain.models.AddGameOfferParams
import com.pawlowski.sportnite.presentation.models.*
import com.pawlowski.sportnite.utils.Resource
import com.pawlowski.sportnite.utils.UiData
import kotlinx.coroutines.flow.Flow

interface IAppRepository {
    fun getIncomingMeetings(sportFilter: Sport? = null): Flow<UiData<List<Meeting>>>
    fun getWeatherForecast(): Flow<UiData<List<WeatherForecastDay>>>
    fun getUserSports(): Flow<UiData<List<Sport>>>
    fun getPlayers(sportFilter: Sport? = null, nameSearch: String? = null, level: AdvanceLevel?): Flow<UiData<List<Player>>>
    fun getGameOffers(sportFilter: Sport? = null): Flow<UiData<List<GameOffer>>>
    fun getOffersToAccept(sportFilter: Sport?): Flow<UiData<GameOfferToAccept>>
    fun getSportObjects(sportFilters: List<Sport>): Flow<UiData<List<SportObject>>>
    fun getPlayerDetails(playerUid: String): Flow<UiData<PlayerDetails>>
    fun getMeetingDetails(meetingUid: String): Flow<UiData<Meeting>>
    fun getUserNotifications(): Flow<UiData<List<Notification>>>

    suspend fun addGameOffer(gameParams: AddGameOfferParams): Resource<Unit>
    suspend fun sendOfferToAccept(offerUid: String): Resource<Unit>
    suspend fun acceptOfferToAccept(offerToAcceptUid: String): Resource<Unit>

}