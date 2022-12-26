package com.pawlowski.sportnite.domain

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.pawlowski.sportnite.*
import com.pawlowski.sportnite.data.auth.IAuthManager
import com.pawlowski.sportnite.data.auth.UserInfoUpdateCache
import com.pawlowski.sportnite.data.mappers.*
import com.pawlowski.sportnite.domain.models.AddGameOfferParams
import com.pawlowski.sportnite.domain.models.UserUpdateInfoParams
import com.pawlowski.sportnite.presentation.models.*
import com.pawlowski.sportnite.type.CreateResponseInput
import com.pawlowski.sportnite.utils.Resource
import com.pawlowski.sportnite.utils.UiData
import com.pawlowski.sportnite.utils.UiText
import com.pawlowski.sportnite.utils.defaultRequestError
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
    private val userInfoUpdateCache: UserInfoUpdateCache,
    private val authManager: IAuthManager,
    private val ioDispatcher: CoroutineDispatcher
): IAppRepository {
    override fun getIncomingMeetings(sportFilter: Sport?): Flow<UiData<List<Meeting>>> = flow {
        emit(UiData.Loading())

        val response = try {
            apolloClient.query(IncomingOffersQuery()).execute()
        }
        catch (e: Exception) {
            e.printStackTrace()
            null
        }
        response?.let { resp ->
            val meetings = resp.data?.incomingOffers?.map {
                it.toMeeting(authManager.getCurrentUserUid()!!)
            }

            meetings?.let {
                emit(UiData.Success(isFresh = true, data = it))
            }?: kotlin.run {
                emit(UiData.Error(message = defaultRequestError))
            }
        }
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
        val myUid = authManager.getCurrentUserUid()!!
        response?.data?.toGameOfferList()?.filter {
            it.owner.uid != myUid
        }?.let {
            it.forEach { Log.d("test", "${it.owner.uid} $myUid") }
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

    override fun getInfoAboutMe(): Flow<User?> {
        return userInfoUpdateCache.cachedUser
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
        validateResult = {
             it.createResponse?.responseId != null
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

    override suspend fun updateUserInfo(params: UserUpdateInfoParams): Resource<Unit> {
        val result = executeApolloMutation(request = {
            apolloClient.mutation(UpdateUserMutation(params.toUpdateUserInput())).execute()
        })
        if(result is Resource.Success) {
            userInfoUpdateCache.markUserInfoAsSaved(
                User(
                    userName = params.name,
                    userPhotoUrl = params.photoUrl?:""
                )
            )
        }
        return result
    }

    private suspend fun <T: Operation.Data>executeApolloMutation(
        request: suspend () -> ApolloResponse<T>,
        validateResult: (T) -> Boolean = {true},
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
            if(!response?.errors.isNullOrEmpty())
            {
                val message = response?.errors?.map {
                    it.message
                }?.reduce { acc, s -> "$acc$s " }
                return@withContext Resource.Error(UiText.NonTranslatable("Error: $message"))
            }
            val responseData = response?.data
            if(responseData != null && !validateResult(responseData))
            {
                return@withContext Resource.Error(defaultRequestError)
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