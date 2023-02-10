package com.pawlowski.choosesportsandlevels.view_model_related.choose_advance_level_screen

import com.pawlowski.models.AdvanceLevel
import org.orbitmvi.orbit.ContainerHost

internal interface IChooseAdvanceLevelScreenViewModel: ContainerHost<ChooseAdvanceLevelScreenUiState, ChooseAdvanceLevelScreenSideEffect> {
    fun selectLevel(level: AdvanceLevel)
    fun continueClick()
    fun navigateBack()
}