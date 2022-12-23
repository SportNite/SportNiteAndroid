package com.pawlowski.sportnite.presentation.view_models_related.account_details_screen

import androidx.lifecycle.ViewModel
import com.pawlowski.sportnite.domain.models.UserUpdateInfoParams
import com.pawlowski.sportnite.presentation.use_cases.UpdateUserInfoUseCase
import com.pawlowski.sportnite.utils.Resource
import com.pawlowski.sportnite.utils.UiDate
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AccountDetailsScreenViewModel @Inject constructor(
    private val updateUserInfoUseCase: UpdateUserInfoUseCase,
): IAccountDetailsScreenViewModel, ViewModel() {
    override val container: Container<AccountDetailsScreenUiState, AccountDetailsScreenSideEffect> =
        container(AccountDetailsScreenUiState())

    override fun changeNameAndSurnameInput(newValue: String) = intent {
        reduce { state.copy(nameAndSurnameInput = newValue) }
    }

    override fun changeDateInput(newValue: UiDate) = intent {
        reduce {
            state.copy(dateOfBirthInput = newValue)
        }
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

    override fun continueClick() = intent {
        //TODO: Validate inputs

        val currentState = state
        val result = updateUserInfoUseCase(UserUpdateInfoParams(name = currentState.nameAndSurnameInput))
        //TODO add loading
        if(result is Resource.Success) {
            postSideEffect(AccountDetailsScreenSideEffect.NavigateToNextScreen)
        }
        else {
            //TODO add errir
        }
    }

    override fun backClick() {
        TODO("Not yet implemented")
    }


}