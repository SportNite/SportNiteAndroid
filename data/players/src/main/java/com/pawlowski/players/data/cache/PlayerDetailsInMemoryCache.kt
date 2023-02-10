package com.pawlowski.players.data.cache

import com.pawlowski.cacheutils.key_based_cache.InMemoryDataCache
import com.pawlowski.models.PlayerDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PlayerDetailsInMemoryCache @Inject constructor(): InMemoryDataCache<PlayerDetails, String>()