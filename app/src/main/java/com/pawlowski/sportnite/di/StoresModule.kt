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