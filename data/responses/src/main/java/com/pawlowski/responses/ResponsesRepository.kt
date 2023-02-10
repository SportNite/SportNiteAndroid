package com.pawlowski.responses

import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.fresh
import com.pawlowski.domainutils.toUiData
import com.pawlowski.localstorage.intelligent_cache.OffersIntelligentInMemoryCache
import com.pawlowski.responses.data.cache.OffersToAcceptIntelligentInMemoryCache
import com.pawlowski.models.*
import com.pawlowski.models.params_models.MeetingsFilter
import com.pawlowski.models.params_models.OffersFilter
import com.pawlowski.network.data.IGraphQLService
import com.pawlowski.utils.Resource
import com.pawlowski.utils.UiData
import com.pawlowski.utils.onSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
internal class ResponsesRepository @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val gameOffersToAcceptStore: Store<OffersFilter, List<GameOfferToAccept>>,
    private val meetingsStore: Store<MeetingsFilter, List<Meeting>>,
    @Named("other") private val offersInMemoryCache: OffersIntelligentInMemoryCache,
    private val offersToAcceptMemoryCache: OffersToAcceptIntelligentInMemoryCache,
    private val graphQLService: IGraphQLService,
): IResponsesRepository {

    override fun getOffersToAccept(sportFilter: Sport?): Flow<UiData<List<GameOfferToAccept>>> {
        return gameOffersToAcceptStore.stream(
            StoreRequest.cached(
                key = OffersFilter(
                    sportFilter = sportFilter
                ), refresh = true
            )
        ).toUiData(isDataEmpty = { it.isNullOrEmpty() })
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
}