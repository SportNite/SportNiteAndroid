package com.pawlowski.sportnite.presentation.view_models_related.find_players_screen

import com.pawlowski.sportnite.presentation.models.AdvanceLevel
import com.pawlowski.sportnite.presentation.models.Sport
import org.orbitmvi.orbit.ContainerHost

interface IFindPlayersScreenViewModel: ContainerHost<FindPlayersScreenUiState, FindPlayersScreenSideEffect> {
    fun changeSearchInput(newValue: String)
    fun changeSportFilterInput(newValue: Sport?)
    fun changeLevelFilterInput(newValue: AdvanceLevel?)
    fun clearFilters()
    fun applyFilters()
}