package com.pawlowski.sportnite.presentation.view_models_related.find_players_screen

import com.pawlowski.models.AdvanceLevel
import com.pawlowski.models.Sport

data class FindPlayersScreenUiState(
    val searchInput: String = "",
    val sportFilterInput: Sport? = null,
    val advanceLevelFilterInput: AdvanceLevel? = null,
    val wereAnyFiltersChangedBeforeApply: Boolean = false,
    val areAnyFiltersOn: Boolean = false
)
