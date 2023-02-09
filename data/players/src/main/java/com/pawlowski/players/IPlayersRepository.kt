package com.pawlowski.players

import androidx.paging.PagingData
import com.pawlowski.models.AdvanceLevel
import com.pawlowski.models.Player
import com.pawlowski.models.PlayerDetails
import com.pawlowski.models.Sport
import com.pawlowski.models.params_models.PlayersFilter
import com.pawlowski.utils.UiData
import kotlinx.coroutines.flow.Flow

interface IPlayersRepository {
    fun getPlayers(sportFilter: Sport? = null, nameSearch: String? = null, level: AdvanceLevel?): Flow<UiData<List<Player>>>
    fun getPlayerDetails(playerUid: String): Flow<UiData<PlayerDetails>>
    fun getPagedPlayers(filters: PlayersFilter): Flow<PagingData<Player>>

}