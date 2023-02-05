package com.pawlowski.sportnite.presentation.view_models_related.choose_advance_level_screen

import com.pawlowski.models.AdvanceLevel
import org.orbitmvi.orbit.ContainerHost

interface IChooseAdvanceLevelScreenViewModel: ContainerHost<ChooseAdvanceLevelScreenUiState, ChooseAdvanceLevelScreenSideEffect> {
    fun selectLevel(level: AdvanceLevel)
    fun continueClick()
    fun navigateBack()
}