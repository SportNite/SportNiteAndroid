package com.pawlowski.localstorage.key_based_cache

import com.pawlowski.models.PlayerDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerDetailsInMemoryCache @Inject constructor(): InMemoryDataCache<PlayerDetails, String>()