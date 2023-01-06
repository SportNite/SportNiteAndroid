package com.pawlowski.sportnite.domain

import android.net.Uri
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.api.Optional
import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.pawlowski.sportnite.*
import com.pawlowski.sportnite.data.auth.IAuthManager
import com.pawlowski.sportnite.data.auth.UserInfoUpdateCache
import com.pawlowski.sportnite.data.firebase_storage.FirebaseStoragePhotoUploader
import com.pawlowski.sportnite.data.local.MeetingsInMemoryCache
import com.pawlowski.sportnite.data.local.OffersInMemoryCache
import com.pawlowski.sportnite.data.local.OffersToAcceptMemoryCache
import com.pawlowski.sportnite.data.mappers.*
import com.pawlowski.sportnite.domain.models.*
import com.pawlowski.sportnite.presentation.mappers.toGameOffer
import com.pawlowski.sportnite.presentation.models.*
import com.pawlowski.sportnite.type.CreateResponseInput
import com.pawlowski.sportnite.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val userInfoUpdateCache: UserInfoUpdateCache,
    private val authManager: IAuthManager,
    private val firebaseStoragePhotoUploader: FirebaseStoragePhotoUploader,
    private val ioDispatcher: CoroutineDispatcher,
    private val playersStore: Store<PlayersFilter, List<Player>>,
    private val offersStore: Store<OffersFilter, List<GameOffer>>,
    private val gameOffersToAcceptStore: Store<OffersFilter, List<GameOfferToAccept>>,
    private val playerDetailsStore: Store<String, PlayerDetails>,
    private val meetingsStore: Store<MeetingsFilter, List<Meeting>>,
    private val meetingsInMemoryCache: MeetingsInMemoryCache,
    private val offersInMemoryCache: OffersInMemoryCache,
    private val offersToAcceptMemoryCache: OffersToAcceptMemoryCache
) : IAppRepository {
    override fun getIncomingMeetings(sportFilter: Sport?): Flow<UiData<List<Meeting>>> {
        return meetingsStore.stream(
            StoreRequest.cached(
                key = MeetingsFilter(
                    sportFilter = sportFilter
                ), refresh = true
            )
        ).toUiData(isDataEmpty = { it.isNullOrEmpty() })
    }

    override fun getWeatherForecast(): Flow<UiData<List<WeatherForecastDay>>> {
        TODO("Not yet implemented")
    }

    override fun getUserSports(): Flow<UiData<List<Sport>>> {
        return userInfoUpdateCache.cachedLevels.map { sportsMap ->
            sportsMap?.keys?.toList()?.let {
                UiData.Success(isFresh = true, data = it)
            }?: kotlin.run {
                UiData.Error()
            }
        }
    }

    override fun getPlayers(
        sportFilter: Sport?,
        nameSearch: String?,
        level: AdvanceLevel?
    ): Flow<UiData<List<Player>>> {
        val myUid = authManager.getCurrentUserUid()!!
        return playersStore.stream(
            StoreRequest.cached(
                key = PlayersFilter(
                    sportFilter = sportFilter,
                    nameSearch = nameSearch,
                    level = level
                ), refresh = true
            )
        ).toUiData(filterPredicateOnListData = {
            it.uid != myUid
        })
    }

    override fun getGameOffers(sportFilter: Sport?): Flow<UiData<List<GameOffer>>> {
        val myUid = authManager.getCurrentUserUid()!!
        return offersStore.stream(
            StoreRequest.cached(
                key = OffersFilter(
                    sportFilter = sportFilter
                ), refresh = true
            )
        ).toUiData(filterPredicateOnListData = {
            it.owner.uid != myUid
        })
    }

    override fun getMyGameOffers(sportFilter: Sport?): Flow<UiData<List<GameOffer>>> {
        return offersStore.stream(
            StoreRequest.cached(
                key = OffersFilter(
                    sportFilter = sportFilter,
                    myOffers = true
                ), refresh = true //TODO: check is it working if something appeared in cache before was loaded (when added an offer)
            )
        ).toUiData(isDataEmpty = { it.isNullOrEmpty() })
    }

    override fun getOffersToAccept(sportFilter: Sport?): Flow<UiData<List<GameOfferToAccept>>> {
        return gameOffersToAcceptStore.stream(
            StoreRequest.cached(
                key = OffersFilter(
                    sportFilter = sportFilter
                ), refresh = true
            )
        ).toUiData(isDataEmpty = { it.isNullOrEmpty() })
    }

    override fun getSportObjects(sportFilters: List<Sport>): Flow<UiData<List<SportObject>>> {
        TODO("Not yet implemented")
    }

    override fun getPlayerDetails(playerUid: String): Flow<UiData<PlayerDetails>> {
        return playerDetailsStore.stream(
            StoreRequest.cached(
                key = playerUid,
                refresh = true
            )
        ).toUiData()
    }

    override fun getMeetingDetails(meetingUid: String): Flow<UiData<Meeting>> = flow {
        //It's collected only from cache because it will be always there (meetings are always fetched before navigating to see their details)
        emit(UiData.Loading())
        meetingsInMemoryCache.observeFirstFromAnyKey {
            it.meetingUid == meetingUid
        }.collect {
            emit(UiData.Success(isFresh = true, data = it))
        }
    }

    override fun getUserNotifications(): Flow<UiData<List<Notification>>> {
        TODO("Not yet implemented")
    }

    override fun getInfoAboutMe(): Flow<User?> {
        return userInfoUpdateCache.cachedUser
    }

    override fun getPagedOffers(): Flow<PagingData<GameOffer>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PagingFactory(
                    request = { page, pageSize ->
                    executeApolloQuery(
                            request = {
                                apolloClient.query(OffersQuery(offerFilterInput = OffersFilter(null, false).toOfferFilterInput(), after = Optional.present(page), first = Optional.present(pageSize))).execute()
                            },
                            mapper = {
                                val pageInfo = it.offers?.pageInfo!!
                                PaginationPage(data = it.toGameOfferList()!!, hasNextPage = pageInfo.hasNextPage, endCursor = pageInfo.endCursor)
                            }
                        )

                    }
                )
            }
        ).flow
    }

    override fun getPagedMeetings(): Flow<PagingData<Meeting>> {
        TODO()
//        return Pager(
//            config = PagingConfig(
//                pageSize = 10,
//                enablePlaceholders = false
//            ),
//            pagingSourceFactory = {
//                PagingFactory(
//                    request = { page, pageSize ->
//                        executeApolloQuery(
//                            request = {
//                                apolloClient.query(
//                                    IncomingOffersQuery(
//                                        offersFilter = MeetingsFilter(null).toOfferFilterInput()
//                                    )
//                                ).execute()
//                            },
//                            mapper = {
//                                val data = it.incomingOffers.let { beforeMappingData ->
//                                    val userUid = authManager.getCurrentUserUid()!!
//                                    beforeMappingData.map {
//                                        it.toMeeting(userUid)
//                                    }
//                                }
//                                //val pageInfo = it.offers?.pageInfo!!
//                                PaginationPage(data = data, hasNextPage = pageInfo.hasNextPage, endCursor = /pageInfo.endCursor)
//                            }
//                        )
//
//                    }
//                )
//            }
//        ).flow
    }


    override suspend fun addGameOffer(gameParams: AddGameOfferParams): Resource<Unit> {
        return executeApolloMutation(request = {
            apolloClient.mutation(CreateOfferMutation(gameParams.toCreateOfferInput())).execute()
        },
            onDataSuccessfullyReceived = {
                //Log.d("New offer id", it.createOffer.offerId.toString())
                val paramsAsGameOffer = gameParams.toGameOffer(
                    offerId = it.createOffer.offerId.toString(),
                    playerName = userInfoUpdateCache.cachedUser.value?.userName?:"")
                offersInMemoryCache.addElement(
                    key = OffersFilter(
                        myOffers = true,
                        sportFilter = null
                    ),
                    element = paramsAsGameOffer
                )
                /*offersInMemoryCache.addElement(
                    key = OffersFilter(
                        myOffers = true,
                        sportFilter = gameParams.sport
                    ),
                    element = paramsAsGameOffer
                )*/
            })
    }

    override suspend fun sendOfferToAccept(offerUid: String): Resource<Unit> {
        return executeApolloMutation(request = {
            apolloClient.mutation(
                CreateResponseMutation(
                    CreateResponseInput(offerId = offerUid, description = "")
                )
            ).execute()
        },
            validateResult = {
                it.createResponse?.responseId != null
            },
            onDataSuccessfullyReceived = {
                Log.d("New response id", it.createResponse?.responseId.toString())
            })
    }

    override suspend fun acceptOfferToAccept(offerToAcceptUid: String): Resource<Unit> {
        //Log.d("offerToAcceptId", offerToAcceptUid)
        return executeApolloMutation(request = {
            apolloClient.mutation(AcceptResponseMutation(responseId = offerToAcceptUid)).execute()
        }).onSuccess {
            offersToAcceptMemoryCache.deleteElementFromAllKeys { it.offerToAcceptUid == offerToAcceptUid }
            //TODO: Add meeting to cache
        }
    }

    override fun signOut() {
        authManager.signOut()
        userInfoUpdateCache.deleteUserInfoCache()
    }

    override suspend fun updateUserInfo(params: UserUpdateInfoParams): Resource<Unit> {
        val uploadedPhotoUri = params.photoUrl?.let {
            val result = firebaseStoragePhotoUploader.uploadNewImage(
                Uri.parse(it),
                authManager.getCurrentUserUid()!!
            )
            result.dataOrNull()
        } ?: return Resource.Error(defaultRequestError)
        val result = executeApolloMutation(request = {
            apolloClient.mutation(
                UpdateUserMutation(
                    params.copy(photoUrl = uploadedPhotoUri).toUpdateUserInput()
                )
            ).execute()
        })
        if (result is Resource.Success) {
            userInfoUpdateCache.markUserInfoAsSaved(
                User(
                    userName = params.name,
                    userPhotoUrl = uploadedPhotoUri,
                    userPhoneNumber = authManager.getUserPhone()
                )
            )
        }
        return result
    }

    override suspend fun updateAdvanceLevelInfo(levels: Map<Sport, AdvanceLevel>): Resource<Unit> {
        return withContext(ioDispatcher) {
            val isAllSuccess = levels.toSetSkillInput().map {
                async {
                    executeApolloMutation(request = {
                        apolloClient.mutation(SetSkillMutation(it)).execute()
                    })
                }
            }.all {
                it.await() is Resource.Success
            }
            if(isAllSuccess)
            {
                userInfoUpdateCache.saveInfoAboutAdvanceLevels(levels)
                Resource.Success(Unit)
            }
            else
                Resource.Error(defaultRequestError)
        }
    }

    override suspend fun deleteMyOffer(offerId: String): Resource<Unit> {
        return withContext(ioDispatcher) {
            executeApolloMutation(request = {
                apolloClient.mutation(DeleteOfferMutation(offerId)).execute()
            }) {
                offersInMemoryCache.deleteElement(OffersFilter(sportFilter = null, myOffers = true)) {
                    it.offerUid == offerId
                }
            }
        }
    }

    private fun <Output> Flow<StoreResponse<Output>>.toUiData(
        isDataEmpty: (Output?) -> Boolean = { it != null }
    ): Flow<UiData<Output>> = flow {
        var lastData: Output? = null
        collect {
            when (it) {
                is StoreResponse.Loading -> {
                    if (isDataEmpty(lastData))
                        emit(UiData.Loading())
                }
                is StoreResponse.Error -> {
                    emit(
                        UiData.Error(
                            cachedData = lastData,
                            message = it.errorMessageOrNull()
                                ?.let { errorMessage -> UiText.NonTranslatable(errorMessage) })
                    )
                }
                is StoreResponse.Data -> {
                    lastData = it.value
                    emit(
                        UiData.Success(
                            isFresh = it.origin == ResponseOrigin.Fetcher,
                            data = it.value
                        )
                    )
                }
                is StoreResponse.NoNewData -> {
                    if (it.origin == ResponseOrigin.Fetcher) {
                        //emit(UiData.Success(isFresh = it.origin == ResponseOrigin.Fetcher, data = null))
                    }
                }
            }
        }
    }



    private fun <Output> Flow<StoreResponse<List<Output>>>.toUiData(
        isDataEmpty: (List<Output>?) -> Boolean = { it != null && it.isNotEmpty() },
        filterPredicateOnListData: (Output) -> Boolean,
    ): Flow<UiData<List<Output>>> {
        return toUiData(isDataEmpty = isDataEmpty).map { data ->
            data.filteredIfDataExists(filterPredicateOnListData)
        }
    }

    private suspend fun <T : Operation.Data> executeApolloMutation(
        request: suspend () -> ApolloResponse<T>,
        validateResult: (T) -> Boolean = { true },
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
            if (!response?.errors.isNullOrEmpty()) {
                val message = response?.errors?.map {
                    it.message
                }?.reduce { acc, s -> "$acc$s " }
                return@withContext Resource.Error(UiText.NonTranslatable("Error: $message"))
            }
            val responseData = response?.data
            if (responseData != null && !validateResult(responseData)) {
                return@withContext Resource.Error(defaultRequestError)
            }
            return@withContext response?.data?.let {
                onDataSuccessfullyReceived(it)
                Resource.Success(Unit)
            } ?: let {
                Resource.Error(
                    message = UiText.NonTranslatable(
                        response?.errors?.firstOrNull()?.message ?: "Request error"
                    )
                )
            }
        }
    }

    private suspend fun <T : Operation.Data, D> executeApolloQuery(
        request: suspend () -> ApolloResponse<T>,
        mapper: (T) -> D
    ): Resource<D> {
        return withContext(ioDispatcher) {
            val response = try {
                request().dataAssertNoErrors
            } catch (e: Exception) {
                ensureActive()
                e.printStackTrace()
                null
            }

            response?.let {
                Resource.Success(mapper(it))
            }?:Resource.Error(defaultRequestError)
        }
    }
}



