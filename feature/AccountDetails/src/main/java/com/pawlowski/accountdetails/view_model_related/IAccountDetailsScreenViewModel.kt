package com.pawlowski.accountdetails.view_model_related

import com.pawlowski.utils.UiDate
import org.orbitmvi.orbit.ContainerHost

interface IAccountDetailsScreenViewModel: ContainerHost<AccountDetailsScreenUiState, AccountDetailsScreenSideEffect> {
    fun changeNameAndSurnameInput(newValue: String)
    fun changeDateInput(newValue: UiDate)
    fun changeIsMaleInput(newValue: Boolean)
    fun changePhotoInput(newValue: String?)
    fun changeTimeAvailabilityInput(newValue: String)
    fun continueClick()
}