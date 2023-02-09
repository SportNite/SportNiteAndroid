package com.pawlowski.repository.di

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.pawlowski.localstorage.intelligent_cache.OffersIntelligentInMemoryCache
import com.pawlowski.models.GameOffer
import com.pawlowski.models.params_models.OffersFilter
import com.pawlowski.network.data.IGraphQLService
import com.pawlowski.repository.IOffersRepository
import com.pawlowski.repository.OffersRepository
import com.pawlowski.utils.dataOrNull
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.ZoneOffset
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OffersModule {

    @Singleton
    @Provides
    internal fun offersRepository(offersRepository: OffersRepository): IOffersRepository = offersRepository

    @Singleton
    @Provides
    fun offersStore(graphQLService: IGraphQLService, @Named("other") offersInMemoryCache: OffersIntelligentInMemoryCache, @Named("my") myOffersInMemoryCache: OffersIntelligentInMemoryCache): Store<OffersFilter, List<GameOffer>> {
        return StoreBuilder.from(fetcher = Fetcher.of { filters: OffersFilter ->
            graphQLService.getOffers(filters, cursor = null, pageSize = 50).dataOrNull()!!.data
        }, sourceOfTruth = SourceOfTruth.of(
            reader = { key: OffersFilter ->
                val cache = when(key.myOffers) {
                    true -> {
                        myOffersInMemoryCache
                    }
                    false -> {
                        offersInMemoryCache
                    }
                }
                cache.observeData(key, sortBy = {
                    it.date.offsetDateTimeDate.toLocalDateTime().toInstant(ZoneOffset.UTC)
                })

            },
            writer = { key: OffersFilter, input: List<GameOffer> ->
                val cache = when(key.myOffers) {
                    true -> {
                        myOffersInMemoryCache
                    }
                    false -> {
                        offersInMemoryCache
                    }
                }
                cache.upsertManyElements(input)
            },
            delete = { key: OffersFilter ->
                val cache = when(key.myOffers) {
                    true -> {
                        myOffersInMemoryCache
                    }
                    false -> {
                        offersInMemoryCache
                    }
                }
                cache.deleteAllElementsWithKey(key)
            },
            deleteAll = {
                myOffersInMemoryCache.clearAll()
                offersInMemoryCache.clearAll()
            }
        )).build()
    }
}