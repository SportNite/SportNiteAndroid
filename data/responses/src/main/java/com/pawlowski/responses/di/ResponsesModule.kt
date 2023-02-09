package com.pawlowski.responses.di

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.pawlowski.localstorage.intelligent_cache.OffersToAcceptIntelligentInMemoryCache
import com.pawlowski.models.GameOfferToAccept
import com.pawlowski.models.params_models.OffersFilter
import com.pawlowski.network.data.IGraphQLService
import com.pawlowski.responses.IResponsesRepository
import com.pawlowski.responses.ResponsesRepository
import com.pawlowski.utils.dataOrNull
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.ZoneOffset
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ResponsesModule {

    @Singleton
    @Provides
    internal fun responsesRepository(responsesRepository: ResponsesRepository): IResponsesRepository = responsesRepository


    @Singleton
    @Provides
    fun offersToAcceptStore(
        graphQLService: IGraphQLService,
        offersToAcceptMemoryCache: OffersToAcceptIntelligentInMemoryCache
    ): Store<OffersFilter, List<GameOfferToAccept>> {
        return StoreBuilder.from(fetcher = Fetcher.of { filters: OffersFilter ->
            graphQLService.getOffersToAccept(filters, cursor = null, pageSize = 50).dataOrNull()!!.data
        }, sourceOfTruth = SourceOfTruth.of(
            reader = { key: OffersFilter ->
                offersToAcceptMemoryCache.observeData(key, sortBy = {
                    it.offer.date.offsetDateTimeDate.toLocalDateTime().toInstant(ZoneOffset.UTC)
                })
            },
            writer = { _: OffersFilter, input: List<GameOfferToAccept> ->
                offersToAcceptMemoryCache.upsertManyElements(input)
            },
            delete = { key: OffersFilter ->
                offersToAcceptMemoryCache.deleteAllElementsWithKey(key)
            },
            deleteAll = { offersToAcceptMemoryCache.clearAll() }
        )).build()
    }
}