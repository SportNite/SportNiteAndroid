package com.pawlowski.choosesportsandlevels.view_model_related.choose_sports_screen

import com.pawlowski.models.Sport

internal data class ChooseSportsScreenUiState(
    val sports: Map<Sport, Boolean>,
    val isLoading: Boolean = false
)
