package com.pawlowski.choosecity.view_model_related

import org.orbitmvi.orbit.ContainerHost

interface IChooseCityScreenViewModel: ContainerHost<ChooseCityScreenUiState, ChooseCityScreenSideEffect> {
    fun changeCityInput(newValue: String)
    fun changeSearchCityInput(newValue: String)
    fun continueClick()
    fun navigateBack()

}