package com.pawlowski.localstorage.key_based_cache

import com.pawlowski.models.params_models.PlayersFilter
import com.pawlowski.models.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayersInMemoryCache @Inject constructor(): InMemoryDataCache<Player, PlayersFilter>()