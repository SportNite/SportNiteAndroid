package com.pawlowski.sportnite.domain

import androidx.paging.PagingData
import com.pawlowski.models.*
import com.pawlowski.sportnite.domain.models.AddGameOfferParams
import com.pawlowski.sportnite.domain.models.OffersFilter
import com.pawlowski.sportnite.domain.models.PlayersFilter
import com.pawlowski.sportnite.domain.models.UserUpdateInfoParams
import com.pawlowski.sportnite.presentation.models.*
import com.pawlowski.utils.Resource
import com.pawlowski.utils.UiData
import kotlinx.coroutines.flow.Flow

interface IAppRepository {
    fun getIncomingMeetings(sportFilter: Sport? = null): Flow<UiData<List<Meeting>>>
    fun getWeatherForecast(): Flow<UiData<List<WeatherForecastDay>>>
    fun getUserSports(): Flow<UiData<List<Sport>>>
    fun getPlayers(sportFilter: Sport? = null, nameSearch: String? = null, level: AdvanceLevel?): Flow<UiData<List<Player>>>
    fun getGameOffers(sportFilter: Sport? = null): Flow<UiData<List<GameOffer>>>
    fun getMyGameOffers(sportFilter: Sport? = null): Flow<UiData<List<GameOffer>>>

    fun getOffersToAccept(sportFilter: Sport?): Flow<UiData<List<GameOfferToAccept>>>
    fun getSportObjects(sportFilters: List<Sport>): Flow<UiData<List<SportObject>>>
    fun getPlayerDetails(playerUid: String): Flow<UiData<PlayerDetails>>
    fun getMeetingDetails(meetingUid: String): Flow<UiData<Meeting>>
    fun getUserNotifications(): Flow<UiData<List<Notification>>>
    fun getInfoAboutMe() : Flow<User?>

    fun getPagedOffers(filter: OffersFilter): Flow<PagingData<GameOffer>>
    fun getPagedMeetings(): Flow<PagingData<Meeting>>


    suspend fun addGameOffer(gameParams: AddGameOfferParams): Resource<Unit>
    suspend fun sendOfferToAccept(offerUid: String): Resource<String>
    suspend fun acceptOfferToAccept(offerToAcceptUid: String): Resource<Unit>
    suspend fun updateUserInfo(params: UserUpdateInfoParams): Resource<Unit>
    suspend fun updateAdvanceLevelInfo(levels: Map<Sport, AdvanceLevel>): Resource<Unit>
    suspend fun deleteMyOffer(offerId: String): Resource<Unit>
    suspend fun deleteMyOfferToAccept(offerToAcceptUid: String): Resource<Unit>
    fun signOut()
    fun getPagedPlayers(filters: PlayersFilter): Flow<PagingData<Player>>
}