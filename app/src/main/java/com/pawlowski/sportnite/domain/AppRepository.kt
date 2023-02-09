package com.pawlowski.sportnite.domain

import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dropbox.android.external.store4.*
import com.pawlowski.auth.IAuthManager
import com.pawlowski.cache.IUserInfoUpdateCache
import com.pawlowski.imageupload.IPhotoUploader
import com.pawlowski.localstorage.intelligent_cache.OffersIntelligentInMemoryCache
import com.pawlowski.localstorage.intelligent_cache.OffersToAcceptIntelligentInMemoryCache
import com.pawlowski.models.*
import com.pawlowski.models.params_models.*
import com.pawlowski.network.data.IGraphQLService
import com.pawlowski.notificationservice.synchronization.INotificationTokenSynchronizer
import com.pawlowski.sportnite.presentation.models.SportObject
import com.pawlowski.utils.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val userInfoUpdateCache: IUserInfoUpdateCache,
    private val authManager: IAuthManager,
    private val photoUploader: IPhotoUploader,
    private val ioDispatcher: CoroutineDispatcher,
    private val playersStore: Store<PlayersFilter, List<Player>>,
    private val gameOffersToAcceptStore: Store<OffersFilter, List<GameOfferToAccept>>,
    private val playerDetailsStore: Store<String, PlayerDetails>,
    private val meetingsStore: Store<MeetingsFilter, List<Meeting>>,
    @Named("other") private val offersInMemoryCache: OffersIntelligentInMemoryCache,
    private val offersToAcceptMemoryCache: OffersToAcceptIntelligentInMemoryCache,
    private val graphQLService: IGraphQLService,
    private val notificationTokenSynchronizer: INotificationTokenSynchronizer
) : IAppRepository {


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





    override fun getPagedNotifications(): Flow<PagingData<UserNotification>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PagingFactory(
                    request = { page, pageSize ->
                        graphQLService.getNotifications(
                            cursor = page,
                            pageSize = pageSize
                        )
                    }
                )
            }
        ).flow
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



    override fun getUserNotifications(): Flow<UiData<List<UserNotification>>> {
        TODO("Not yet implemented")
    }

    override fun getInfoAboutMe(): Flow<User?> {
        return userInfoUpdateCache.cachedUser
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






    override suspend fun sendOfferToAccept(offerUid: String): Resource<String> {
        return graphQLService.sendOfferToAccept(offerUid)
            .onSuccess {
                offersInMemoryCache.updateElementsIf(
                    predicate = { offer ->
                        offer.offerUid == offerUid
                    },
                    newValue = { offer ->
                        offer.copy(myResponseIdIfExists = it)
                    }
                )

            }
    }

    override suspend fun acceptOfferToAccept(offerToAcceptUid: String): Resource<Unit> {
        return graphQLService.acceptOfferToAccept(offerToAcceptUid)
            .onSuccess {
                offersToAcceptMemoryCache.deleteElementsIf { it.offerToAcceptUid == offerToAcceptUid }
                meetingsStore.fresh(MeetingsFilter(sportFilter = null))
                //TODO: Add meeting to cache instead of refreshing?
            }
    }

    override fun signOut() {
        authManager.signOut()
        userInfoUpdateCache.deleteUserInfoCache()
        notificationTokenSynchronizer.deleteCurrentToken()
    }

    override suspend fun updateUserInfo(params: UserUpdateInfoParams): Resource<Unit> {
        val uploadedPhotoUri = params.photoUrl?.let {
            val result = photoUploader.uploadNewImage(
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



    override suspend fun deleteMyOfferToAccept(offerToAcceptUid: String): Resource<Unit> {
        return withContext(ioDispatcher) {
            graphQLService.deleteMyOfferToAccept(offerToAcceptUid)
                .onSuccess {
                    offersInMemoryCache.updateElementsIf(
                        predicate = { offer ->
                            offer.myResponseIdIfExists == offerToAcceptUid
                        },
                        newValue = { offer ->
                            offer.copy(myResponseIdIfExists = null)
                        }
                    )

                }
        }
    }

    override suspend fun rejectOfferToAccept(offerToAcceptUid: String): Resource<Unit> {
        return graphQLService.rejectOfferToAccept(offerToAcceptUid= offerToAcceptUid)
            .onSuccess {
                offersToAcceptMemoryCache.deleteElementsIf {
                    it.offerToAcceptUid == offerToAcceptUid
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



