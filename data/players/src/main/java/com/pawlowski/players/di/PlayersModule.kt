package com.pawlowski.players.di

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.pawlowski.localstorage.key_based_cache.PlayerDetailsInMemoryCache
import com.pawlowski.localstorage.key_based_cache.PlayersInMemoryCache
import com.pawlowski.models.Player
import com.pawlowski.models.PlayerDetails
import com.pawlowski.models.params_models.PlayersFilter
import com.pawlowski.network.data.IGraphQLService
import com.pawlowski.players.IPlayersRepository
import com.pawlowski.players.PlayersRepository
import com.pawlowski.utils.dataOrNull
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlayersModule {

    @Singleton
    @Provides
    internal fun playersRepository(playersRepository: PlayersRepository): IPlayersRepository = playersRepository

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
}