package com.pawlowski.findplayerrs.view_model_related

import androidx.paging.PagingData
import com.pawlowski.models.AdvanceLevel
import com.pawlowski.models.Player
import com.pawlowski.models.Sport
import kotlinx.coroutines.flow.Flow
import org.orbitmvi.orbit.ContainerHost

interface IFindPlayersScreenViewModel: ContainerHost<FindPlayersScreenUiState, FindPlayersScreenSideEffect> {
    fun changeSearchInput(newValue: String)
    fun changeSportFilterInput(newValue: Sport?)
    fun changeLevelFilterInput(newValue: AdvanceLevel?)
    fun clearFilters()
    fun applyFilters()
    val pagedPlayers: Flow<PagingData<Player>>
}