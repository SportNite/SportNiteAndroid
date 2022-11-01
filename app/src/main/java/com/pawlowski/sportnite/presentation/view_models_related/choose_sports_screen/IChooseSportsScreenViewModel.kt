package com.pawlowski.sportnite.presentation.view_models_related.choose_sports_screen

import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.sportnite.presentation.view_models_related.choose_city_screen.ChooseCityScreenSideEffect
import org.orbitmvi.orbit.ContainerHost

interface IChooseSportsScreenViewModel: ContainerHost<ChooseSportsScreenUiState, ChooseCityScreenSideEffect> {
    fun changeSelectionOfSport(sport: Sport)
    fun continueClick()
    fun navigateBack()
}