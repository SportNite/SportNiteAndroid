package com.pawlowski.sportnite.domain

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.pawlowski.sportnite.CreateOfferMutation
import com.pawlowski.sportnite.data.mappers.toCreateOfferInput
import com.pawlowski.sportnite.domain.models.AddGameOfferParams
import com.pawlowski.sportnite.presentation.models.*
import com.pawlowski.sportnite.utils.Resource
import com.pawlowski.sportnite.utils.UiData
import com.pawlowski.sportnite.utils.UiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val ioDispatcher: CoroutineDispatcher
): IAppRepository {
    override fun getIncomingMeetings(sportFilter: Sport?): Flow<UiData<List<Meeting>>> {
        TODO("Not yet implemented")
    }

    override fun getWeatherForecast(): Flow<UiData<List<WeatherForecastDay>>> {
        TODO("Not yet implemented")
    }

    override fun getUserSports(): Flow<UiData<List<Sport>>> {
        TODO("Not yet implemented")
    }

    override fun getPlayers(
        sportFilter: Sport?,
        nameSearch: String?,
        level: AdvanceLevel?
    ): Flow<UiData<List<Player>>> {
        TODO("Not yet implemented")
    }

    override fun getGameOffers(sportFilter: Sport?): Flow<UiData<List<GameOffer>>> {
        TODO("Not yet implemented")
    }

    override fun getOffersToAccept(sportFilter: Sport?): Flow<UiData<GameOfferToAccept>> {
        TODO("Not yet implemented")
    }

    override fun getSportObjects(sportFilters: List<Sport>): Flow<UiData<List<SportObject>>> {
        TODO("Not yet implemented")
    }

    override fun getPlayerDetails(playerUid: String): Flow<UiData<PlayerDetails>> {
        TODO("Not yet implemented")
    }

    override fun getMeetingDetails(meetingUid: String): Flow<UiData<Meeting>> {
        TODO("Not yet implemented")
    }

    override fun getUserNotifications(): Flow<UiData<List<Notification>>> {
        TODO("Not yet implemented")
    }

    override suspend fun addGameOffer(gameParams: AddGameOfferParams): Resource<Unit> {
        return withContext(ioDispatcher) {
            val response = try {
                apolloClient.mutation(CreateOfferMutation(gameParams.toCreateOfferInput())).execute()
            } catch (e: Exception) {
                ensureActive()
                e.printStackTrace()
                null
            }
            return@withContext response?.data?.let {
                Log.d("New offer id", it.createOffer.offerId.toString())

                Resource.Success(Unit)
            }?:let {
                Resource.Error(message = UiText.NonTranslatable(response?.errors?.firstOrNull()?.message?:"Request error"))
            }
        }
    }

    override suspend fun sendOfferToAccept(offerUid: String): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun acceptOfferToAccept(offerToAcceptUid: String): Resource<Unit> {
        TODO("Not yet implemented")
    }
}