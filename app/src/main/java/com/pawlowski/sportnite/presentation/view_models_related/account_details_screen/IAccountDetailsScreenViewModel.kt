package com.pawlowski.sportnite.presentation.view_models_related.account_details_screen

import com.pawlowski.sportnite.utils.UiDate
import org.orbitmvi.orbit.ContainerHost

interface IAccountDetailsScreenViewModel: ContainerHost<AccountDetailsScreenUiState, AccountDetailsScreenSideEffect> {
    fun changeNameAndSurnameInput(newValue: String)
    fun changeDateInput(newValue: UiDate)
    fun changeIsMaleInput(newValue: Boolean)
    fun changePhotoInput(newValue: String?)
    fun changeTimeAvailabilityInput(newValue: String)
    fun continueClick()
    fun backClick()
}