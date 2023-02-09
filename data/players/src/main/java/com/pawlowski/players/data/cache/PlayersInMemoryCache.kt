package com.pawlowski.players.data.cache

import com.pawlowski.cacheutils.key_based_cache.InMemoryDataCache
import com.pawlowski.models.params_models.PlayersFilter
import com.pawlowski.models.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PlayersInMemoryCache @Inject constructor(): InMemoryDataCache<Player, PlayersFilter>()