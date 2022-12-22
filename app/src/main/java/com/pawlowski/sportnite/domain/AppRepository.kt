package com.pawlowski.sportnite.domain

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.pawlowski.sportnite.*
import com.pawlowski.sportnite.data.mappers.toCreateOfferInput
import com.pawlowski.sportnite.data.mappers.toGameOfferList
import com.pawlowski.sportnite.data.mappers.toSportType
import com.pawlowski.sportnite.domain.models.AddGameOfferParams
import com.pawlowski.sportnite.presentation.models.*
import com.pawlowski.sportnite.type.CreateResponseInput
import com.pawlowski.sportnite.utils.Resource
import com.pawlowski.sportnite.utils.UiData
import com.pawlowski.sportnite.utils.UiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val ioDispatcher: CoroutineDispatcher
): IAppRepository {
    override fun getIncomingMeetings(sportFilter: Sport?): Flow<UiData<List<Meeting>>> = flow {
        emit(UiData.Loading())
        //TODO
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

    override fun getGameOffers(sportFilter: Sport?): Flow<UiData<List<GameOffer>>> = flow {
        emit(UiData.Loading())
        val response = try {
            apolloClient.query(OffersQuery()).execute()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        response?.data?.toGameOfferList()?.let {
            emit(UiData.Success(isFresh = true, data = it))
        }
    }

    override fun getOffersToAccept(sportFilter: Sport?): Flow<UiData<List<GameOfferToAccept>>> = flow {
        emit(UiData.Loading())
        //Optional.presentIfNotNull(sportFilter?.toSportType())
        val response = try {
            apolloClient.query(ResponsesQuery(sportFilter!!.toSportType())).execute()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        response?.data
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
        return executeApolloMutation(request = {
            apolloClient.mutation(CreateOfferMutation(gameParams.toCreateOfferInput())).execute()
        },
        onDataSuccessfullyReceived = {
            Log.d("New offer id", it.createOffer.offerId.toString())
        })
    }

    override suspend fun sendOfferToAccept(offerUid: String): Resource<Unit> {
        return executeApolloMutation(request = {
            apolloClient.mutation(CreateResponseMutation(
                CreateResponseInput(offerId = offerUid, description = "")
            )).execute()
        },
        onDataSuccessfullyReceived = {
            Log.d("New response id", it.createResponse?.responseId.toString())
        })
    }

    override suspend fun acceptOfferToAccept(offerToAcceptUid: String): Resource<Unit> {
        return executeApolloMutation(request = {
            apolloClient.mutation(AcceptResponseMutation(responseId = offerToAcceptUid)).execute()
        })
    }

    private suspend fun <T: Operation.Data>executeApolloMutation(
        request: suspend () -> ApolloResponse<T>,
        onDataSuccessfullyReceived: (T) -> Unit = {},
    ): Resource<Unit> {
        return withContext(ioDispatcher) {
            val response = try {
                request()
            } catch (e: Exception) {
                ensureActive()
                e.printStackTrace()
                null
            }
            return@withContext response?.data?.let {
                onDataSuccessfullyReceived(it)
                Resource.Success(Unit)
            }?:let {
                Resource.Error(message = UiText.NonTranslatable(response?.errors?.firstOrNull()?.message?:"Request error"))
            }
        }
    }
}