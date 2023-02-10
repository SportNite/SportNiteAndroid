package com.pawlowski.findplayerrs.view_model_related

import com.pawlowski.models.AdvanceLevel
import com.pawlowski.models.Sport

data class FindPlayersScreenUiState(
    val searchInput: String = "",
    val sportFilterInput: Sport? = null,
    val advanceLevelFilterInput: AdvanceLevel? = null,
    val wereAnyFiltersChangedBeforeApply: Boolean = false,
    val areAnyFiltersOn: Boolean = false
)
