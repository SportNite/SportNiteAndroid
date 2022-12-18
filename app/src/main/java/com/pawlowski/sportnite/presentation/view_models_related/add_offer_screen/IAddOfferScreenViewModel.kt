package com.pawlowski.sportnite.presentation.view_models_related.add_offer_screen

import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.sportnite.utils.UiDate
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