package com.pawlowski.repository.di

import com.dropbox.android.external.store4.*
import com.pawlowski.localstorage.intelligent_cache.OffersIntelligentInMemoryCache
import com.pawlowski.models.GameOffer
import com.pawlowski.models.params_models.OffersFilter
import com.pawlowski.network.data.IGraphQLService
import com.pawlowski.repository.IOffersRepository
import com.pawlowski.repository.OffersRepository
import com.pawlowski.repository.use_cases.AddGameOfferUseCase
import com.pawlowski.repository.use_cases.DeleteMyOfferUseCase
import com.pawlowski.repository.use_cases.GetGameOffersUseCase
import com.pawlowski.repository.use_cases.GetMyOffersUseCase
import com.pawlowski.repository.use_cases.GetPagedOffersUseCase
import com.pawlowski.repository.use_cases.RefreshOffersUseCase
import com.pawlowski.utils.Resource
import com.pawlowski.utils.UiText
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

    @Singleton
    @Provides
    fun AddGameOfferUseCase(appRepository: IOffersRepository): AddGameOfferUseCase = AddGameOfferUseCase(appRepository::addGameOffer)

    @Singleton
    @Provides
    fun getGameOffersUseCase(appRepository: IOffersRepository): GetGameOffersUseCase = GetGameOffersUseCase(appRepository::getGameOffers)

    @Singleton
    @Provides
    fun getMyOffersUseCase(appRepository: IOffersRepository): GetMyOffersUseCase = GetMyOffersUseCase(appRepository::getMyGameOffers)

    @Singleton
    @Provides
    fun deleteMyOfferUseCase(appRepository: IOffersRepository): DeleteMyOfferUseCase = DeleteMyOfferUseCase(appRepository::deleteMyOffer)

    @Singleton
    @Provides
    fun getPagedOffersUseCase(appRepository: IOffersRepository) = GetPagedOffersUseCase(appRepository::getPagedOffers)

    @Singleton
    @Provides
    fun refreshOffersUseCase(offersStore: Store<OffersFilter, List<GameOffer>>) = RefreshOffersUseCase {
        try {
            offersStore.fresh(it)
            Resource.Success(Unit)
        }
        catch (e: Exception) {
            Resource.Error(message = UiText.NonTranslatable(e.message?:e.toString()))
        }
    }

}