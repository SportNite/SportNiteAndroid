package com.pawlowski.sportnite.presentation.view_models_related.choose_advance_level_screen

import com.pawlowski.sportnite.presentation.models.AdvanceLevel
import org.orbitmvi.orbit.ContainerHost

interface IChooseAdvanceLevelScreenViewModel: ContainerHost<ChooseAdvanceLevelScreenUiState, ChooseAdvanceLevelScreenSideEffect> {
    fun selectLevel(level: AdvanceLevel)
    fun changeSearchCityInput(newValue: String)
    fun continueClick()
    fun navigateBack()
}