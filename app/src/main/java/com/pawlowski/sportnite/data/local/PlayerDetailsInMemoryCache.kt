package com.pawlowski.sportnite.data.local

import com.pawlowski.models.PlayerDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerDetailsInMemoryCache @Inject constructor(): InMemoryDataCache<PlayerDetails, String>()