package com.pawlowski.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.pawlowski.auth.IAuthManager
import com.pawlowski.cache.IUserInfoUpdateCache
import com.pawlowski.localstorage.intelligent_cache.OffersIntelligentInMemoryCache
import com.pawlowski.models.*
import com.pawlowski.models.mappers.toGameOffer
import com.pawlowski.models.params_models.AddGameOfferParams
import com.pawlowski.models.params_models.OffersFilter
import com.pawlowski.network.data.IGraphQLService
import com.pawlowski.utils.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

internal class OffersRepository @Inject constructor(
    private val userInfoUpdateCache: IUserInfoUpdateCache,
    private val authManager: IAuthManager,
    private val ioDispatcher: CoroutineDispatcher,
    private val offersStore: Store<OffersFilter, List<GameOffer>>,
    @Named("my") private val myOffersInMemoryCache: OffersIntelligentInMemoryCache,
    private val graphQLService: IGraphQLService,
): IOffersRepository {
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

    override suspend fun deleteMyOffer(offerId: String): Resource<Unit> {

        return withContext(ioDispatcher) {
            graphQLService.deleteMyOffer(offerId)
                .onSuccess {
                    myOffersInMemoryCache.deleteElementsIf { it.offerUid == offerId }
                }
        }
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

    override suspend fun addGameOffer(gameParams: AddGameOfferParams): Resource<Unit> {
        return graphQLService.createOffer(gameParams)
            .onSuccess {
                val paramsAsGameOffer = gameParams.toGameOffer(
                    offerId = it,
                    playerName = userInfoUpdateCache.cachedUser.value?.userName?:""
                )

                myOffersInMemoryCache.upsertElement(
                    element = paramsAsGameOffer
                )

            }.asUnitResource()
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