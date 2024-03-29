package com.pawlowski.accountdetails.view_model_related

import androidx.lifecycle.ViewModel
import com.pawlowski.models.params_models.UserUpdateInfoParams
import com.pawlowski.user.use_cases.UpdateUserInfoUseCase
import com.pawlowski.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
internal class AccountDetailsScreenViewModel @Inject constructor(
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

    override fun changePhotoInput(newValue: String?) = intent {
        reduce { state.copy(photo = newValue) }
    }

    override fun changeTimeAvailabilityInput(newValue: String) = intent {
        reduce { state.copy(timeAvailabilityInput = newValue) }
    }

    override fun continueClick() = intent {
        val currentState = state
        if(currentState.isLoading)
            return@intent

        reduce {
            state.copy(isLoading = true)
        }
        if(currentState.nameAndSurnameInput.trim().isEmpty() || currentState.dateOfBirthInput == null)
        {
            postSideEffect(AccountDetailsScreenSideEffect.ShowToastMessage(someFieldsMissingText))
            reduce {
                state.copy(isLoading = false)
            }
            return@intent
        }
        else if(currentState.photo.isNullOrEmpty())
        {
            postSideEffect(AccountDetailsScreenSideEffect.ShowToastMessage(photoRequiredText))
            reduce {
                state.copy(isLoading = false)
            }
            return@intent
        }

        val result = updateUserInfoUseCase(
            UserUpdateInfoParams(
            name = currentState.nameAndSurnameInput.trim(),
            birthDate = currentState.dateOfBirthInput,
            availability = currentState.timeAvailabilityInput.trim(),
            photoUrl = currentState.photo
        )
        )


        result.onSuccess {
            postSideEffect(AccountDetailsScreenSideEffect.NavigateToNextScreen)

        }.onError { message, _ ->
            postSideEffect(AccountDetailsScreenSideEffect.ShowToastMessage(message))
        }
        reduce {
            state.copy(isLoading = false)
        }
    }

}