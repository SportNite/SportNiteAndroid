package com.pawlowski.sportnite.di

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.pawlowski.auth.IAuthManager
import com.pawlowski.localstorage.intelligent_cache.MeetingsIntelligentInMemoryCache
import com.pawlowski.localstorage.intelligent_cache.OffersIntelligentInMemoryCache
import com.pawlowski.localstorage.intelligent_cache.OffersToAcceptIntelligentInMemoryCache
import com.pawlowski.localstorage.key_based_cache.*
import com.pawlowski.models.*
import com.pawlowski.sportnite.*
import com.pawlowski.sportnite.data.local.*
import com.pawlowski.network.data.IGraphQLService
import com.pawlowski.models.params_models.MeetingsFilter
import com.pawlowski.models.params_models.OffersFilter
import com.pawlowski.models.params_models.PlayersFilter
import com.pawlowski.sportnite.presentation.models.*
import com.pawlowski.utils.dataOrNull
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.map
import java.time.ZoneOffset
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StoresModule {
    @Singleton
    @Provides
    fun playersStore(graphQLService: IGraphQLService, playersInMemoryCache: PlayersInMemoryCache): Store<PlayersFilter, List<Player>> {
        return StoreBuilder.from(fetcher = Fetcher.of { filters: PlayersFilter ->
            graphQLService.getPlayers(
                filters = filters,
                cursor = null,
                pageSize = 10
            ).dataOrNull()!!.data
        }, sourceOfTruth = SourceOfTruth.of(
            reader = { key: PlayersFilter ->
                playersInMemoryCache.observeData(key)
            },
            writer = { key: PlayersFilter, input: List<Player> ->
                playersInMemoryCache.addManyElements(key, input) {
                    it.uid
                }
            },
            delete = { key: PlayersFilter ->
                playersInMemoryCache.deleteAllElementsWithKey(key)
            },
            deleteAll = { playersInMemoryCache.deleteAllData() }
        )).build()
    }

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

    @Singleton
    @Provides
    fun playerDetailsStore(
        graphQLService: IGraphQLService,
        playerDetailsInMemoryCache: PlayerDetailsInMemoryCache
    ): Store<String, PlayerDetails> {
        return StoreBuilder.from(fetcher = Fetcher.of { filter: String ->
            graphQLService.getPlayerDetails(filter).dataOrNull()!!
        }, sourceOfTruth = SourceOfTruth.of(
            reader = { key: String ->
                playerDetailsInMemoryCache.observeData(key).map { it.first() }
            },
            writer = { key: String, input: PlayerDetails ->
                playerDetailsInMemoryCache.addManyElements(key, listOf(input))
                {
                    it.playerUid
                }
            },
            delete = { key: String ->
                playerDetailsInMemoryCache.deleteAllElementsWithKey(key)
            },
            deleteAll = { playerDetailsInMemoryCache.deleteAllData() }
        )).build()
    }


    @Singleton
    @Provides
    fun meetingStore(
        graphQLService: IGraphQLService,
        meetingsInMemoryCache: MeetingsIntelligentInMemoryCache,
        authManager: IAuthManager
    ): Store<MeetingsFilter, List<Meeting>> {
        return StoreBuilder.from(fetcher = Fetcher.of { filters: MeetingsFilter ->
            val userUid = authManager.getCurrentUserUid()!!
            graphQLService.getIncomingMeetings(filters, userUid).dataOrNull()!!
        }, sourceOfTruth = SourceOfTruth.of(
            reader = { key: MeetingsFilter ->
                meetingsInMemoryCache.observeData(key, sortBy= {
                    it.date.offsetDateTimeDate.toLocalDateTime().toInstant(ZoneOffset.UTC)
                })
            },
            writer = { _: MeetingsFilter, input: List<Meeting> ->
                meetingsInMemoryCache.upsertManyElements(input)
            },
            delete = { key: MeetingsFilter ->
                meetingsInMemoryCache.deleteAllElementsWithKey(key)
            },
            deleteAll = { meetingsInMemoryCache.clearAll() }
        )).build()
    }
}