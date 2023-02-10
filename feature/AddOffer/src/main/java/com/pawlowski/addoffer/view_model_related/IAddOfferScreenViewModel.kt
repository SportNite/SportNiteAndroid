package com.pawlowski.addoffer.view_model_related

import com.pawlowski.models.Sport
import com.pawlowski.utils.UiDate
import org.orbitmvi.orbit.ContainerHost

interface IAddOfferScreenViewModel: ContainerHost<AddOfferScreenUiState, AddOfferScreenSideEffect> {
    fun changeDateTimeInput(newValue: UiDate)
    fun changeSport(newValue: Sport)
    fun changeCityInput(newValue: String)
    fun changePlaceOrAddressInput(newValue: String)
    fun changeAdditionalNotesInput(newValue: String)
    fun navigateBackClick()
    fun addOfferClick()
}