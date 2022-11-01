package com.pawlowski.sportnite.presentation.view_models_related.choose_sports_screen

import com.pawlowski.sportnite.presentation.models.Sport

data class ChooseSportsScreenUiState(
    val sports: Map<Sport, Boolean>
)
