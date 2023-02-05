package com.pawlowski.sportnite.domain

import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData

import com.dropbox.android.external.store4.*
import com.pawlowski.models.*
import com.pawlowski.models.params_models.*
import com.pawlowski.auth.IAuthManager
import com.pawlowski.sportnite.data.auth.UserInfoUpdateCache
import com.pawlowski.sportnite.data.firebase_storage.FirebaseStoragePhotoUploader
import com.pawlowski.sportnite.data.local.MeetingsInMemoryCache
import com.pawlowski.sportnite.data.local.OffersInMemoryCache
import com.pawlowski.sportnite.data.local.OffersToAcceptMemoryCache
import com.pawlowski.network.data.IGraphQLService
import com.pawlowski.models.mappers.toGameOffer
import com.pawlowski.sportnite.presentation.models.SportObject
import com.pawlowski.utils.defaultRequestError
import com.pawlowski.utils.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
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
    private val offersToAcceptMemoryCache: OffersToAcceptMemoryCache,
    private val graphQLService: IGraphQLService,
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
        }, isDataEmpty = { it.isNullOrEmpty() })
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
        }, isDataEmpty = { it.isNullOrEmpty() })
    }

    override fun getMyGameOffers(sportFilter: Sport?): Flow<UiData<List<GameOffer>>> {
        return offersStore.stream(
            StoreRequest.cached(
                key = OffersFilter(
                    sportFilter = sportFilter,
                    myOffers = true
                ), refresh = true
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

    override fun getPagedOffers(filter: OffersFilter): Flow<PagingData<GameOffer>> {
        val myUid = authManager.getCurrentUserUid()!!
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PagingFactory(
                    request = { page, pageSize ->
                        graphQLService.getOffers(
                            filters = filter,
                            cursor = page,
                            pageSize = pageSize
                        ).filterIfSuccess {
                            filter.myOffers || it.owner.uid != myUid
                        }
                    }
                )
            }
        ).flow
    }

    override fun getPagedPlayers(filters: PlayersFilter): Flow<PagingData<Player>> {
        val myUid = authManager.getCurrentUserUid()!!
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PagingFactory(
                    request = { page, pageSize ->
                        graphQLService.getPlayers(
                            filters = filters,
                            cursor = page,
                            pageSize = pageSize
                        ).filterIfSuccess {
                            it.uid != myUid
                        }
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
        return graphQLService.createOffer(gameParams)
            .onSuccess {
                val paramsAsGameOffer = gameParams.toGameOffer(
                    offerId = it!!,
                    playerName = userInfoUpdateCache.cachedUser.value?.userName?:""
                )

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
            }.asUnitResource()
    }

    override suspend fun sendOfferToAccept(offerUid: String): Resource<String> {
        return graphQLService.sendOfferToAccept(offerUid)
            .onSuccess {
                offersInMemoryCache.updateElements { _, offer ->
                    if(offer.offerUid == offerUid)
                    {
                        offer.copy(myResponseIdIfExists = it)
                    }
                    else
                        offer
                }
            }
    }

    override suspend fun acceptOfferToAccept(offerToAcceptUid: String): Resource<Unit> {
        return graphQLService.acceptOfferToAccept(offerToAcceptUid)
            .onSuccess {
                offersToAcceptMemoryCache.deleteElementFromAllKeys { it.offerToAcceptUid == offerToAcceptUid }
                meetingsStore.fresh(MeetingsFilter(sportFilter = null))
                //TODO: Add meeting to cache or refresh cache with sportFilter
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
        val result = graphQLService.updateUserInfo(params.copy(photoUrl = uploadedPhotoUri))

        return result.onSuccess {
            userInfoUpdateCache.markUserInfoAsSaved(
                User(
                    userName = params.name,
                    userPhotoUrl = uploadedPhotoUri,
                    userPhoneNumber = authManager.getUserPhone()
                )
            )
        }
    }

    override suspend fun updateAdvanceLevelInfo(levels: Map<Sport, AdvanceLevel>): Resource<Unit> {
        return withContext(ioDispatcher) {
            val isAllSuccess = levels.map {
                async {
                    graphQLService.updateAdvanceLevelInfo(it.toPair())
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
            graphQLService.deleteMyOffer(offerId)
                .onSuccess {
                    offersInMemoryCache.deleteElement(OffersFilter(sportFilter = null, myOffers = true)) {
                        it.offerUid == offerId
                    }
                }
        }
    }

    override suspend fun deleteMyOfferToAccept(offerToAcceptUid: String): Resource<Unit> {
        return withContext(ioDispatcher) {
            graphQLService.deleteMyOfferToAccept(offerToAcceptUid)
                .onSuccess {
                    offersInMemoryCache.updateElements { _, offer ->
                        if(offer.myResponseIdIfExists == offerToAcceptUid)
                        {
                            offer.copy(myResponseIdIfExists = null)
                        }
                        else
                            offer
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

}



