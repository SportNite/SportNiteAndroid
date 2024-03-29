package com.pawlowski.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import com.pawlowski.auth.ILightAuthManager
import com.pawlowski.domainutils.PagingKeyBasedFactory
import com.pawlowski.domainutils.toUiData
import com.pawlowski.localstorage.intelligent_cache.OffersIntelligentInMemoryCache
import com.pawlowski.models.*
import com.pawlowski.models.mappers.toGameOffer
import com.pawlowski.models.params_models.AddGameOfferParams
import com.pawlowski.models.params_models.OffersFilter
import com.pawlowski.network.data.IGraphQLService
import com.pawlowski.user.IUserRepository
import com.pawlowski.utils.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

internal class OffersRepository @Inject constructor(
    private val userRepository: IUserRepository,
    private val authManager: ILightAuthManager,
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
                PagingKeyBasedFactory(
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
                    playerName = userRepository.getInfoAboutMe().first()?.userName?:""
                )

                myOffersInMemoryCache.upsertElement(
                    element = paramsAsGameOffer
                )

            }.asUnitResource()
    }


}