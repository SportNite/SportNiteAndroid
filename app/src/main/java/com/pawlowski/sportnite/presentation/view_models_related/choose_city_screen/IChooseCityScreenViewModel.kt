package com.pawlowski.sportnite.presentation.view_models_related.choose_city_screen

import org.orbitmvi.orbit.ContainerHost

interface IChooseCityScreenViewModel: ContainerHost<ChooseCityScreenUiState, ChooseCityScreenSideEffect> {
    fun changeCityInput(newValue: String)
    fun changeSearchCityInput(newValue: String)
    fun continueClick()
    fun navigateBack()

}