package com.pawlowski.sportnite.presentation.view_models_related.account_details_screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AccountDetailsScreenViewModel @Inject constructor(): IAccountDetailsScreenViewModel, ViewModel() {
    override val container: Container<AccountDetailsScreenUiState, AccountDetailsScreenSideEffect> =
        container(AccountDetailsScreenUiState())

    override fun changeNameAndSurnameInput(newValue: String) {
        TODO("Not yet implemented")
    }

    override fun changeDateInput(newValue: String) {
        TODO("Not yet implemented")
    }

    override fun changeIsMaleInput(newValue: Boolean) {
        TODO("Not yet implemented")
    }

    override fun changePhotoInput(newValue: String) {
        TODO("Not yet implemented")
    }

    override fun changeTimeAvailabilityInput(newValue: String) {
        TODO("Not yet implemented")
    }

    override fun continueClick() {
        TODO("Not yet implemented")
    }

    override fun backClick() {
        TODO("Not yet implemented")
    }


}