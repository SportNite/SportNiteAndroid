package com.pawlowski.sportnite.data.local

import com.pawlowski.sportnite.domain.models.PlayersFilter
import com.pawlowski.models.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayersInMemoryCache @Inject constructor(): InMemoryDataCache<Player, PlayersFilter>()