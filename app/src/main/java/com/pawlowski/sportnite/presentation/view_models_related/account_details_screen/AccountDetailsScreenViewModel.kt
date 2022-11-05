package com.pawlowski.sportnite.presentation.view_models_related.account_details_screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AccountDetailsScreenViewModel @Inject constructor(): IAccountDetailsScreenViewModel, ViewModel() {
    override val container: Container<AccountDetailsScreenUiState, AccountDetailsScreenSideEffect> =
        container(AccountDetailsScreenUiState())

    override fun changeNameAndSurnameInput(newValue: String) = intent {
        reduce { state.copy(nameAndSurnameInput = newValue) }
    }

    override fun changeDateInput(newValue: String) = intent {
        //reduce { state.copy(dateOfBirthInput = UiDa) }
        TODO()
    }

    override fun changeIsMaleInput(newValue: Boolean) = intent {
        reduce { state.copy(isMaleInput = newValue) }
    }

    override fun changePhotoInput(newValue: String) = intent {
        reduce { state.copy(photo = newValue) }
    }

    override fun changeTimeAvailabilityInput(newValue: String) = intent {
        reduce { state.copy(timeAvailabilityInput = newValue) }
    }

    override fun continueClick() {
        TODO("Not yet implemented")
    }

    override fun backClick() {
        TODO("Not yet implemented")
    }


}