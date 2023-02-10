package com.pawlowski.choosesportsandlevels.view_model_related.choose_advance_level_screen

import com.pawlowski.models.AdvanceLevel
import com.pawlowski.models.Sport

internal data class ChooseAdvanceLevelScreenUiState(
    val currentSport: Sport,
    val availableLevels: List<AdvanceLevel>? = null,
    val chosenLevel: AdvanceLevel? = null,
    val isLoading: Boolean = false
)
