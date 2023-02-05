package com.pawlowski.sportnite.presentation.view_models_related.choose_sports_screen

import com.pawlowski.models.Sport
import org.orbitmvi.orbit.ContainerHost

interface IChooseSportsScreenViewModel: ContainerHost<ChooseSportsScreenUiState, ChooseSportsScreenSideEffect> {
    fun changeSelectionOfSport(sport: Sport)
    fun continueClick()
    fun navigateBack()
}