package com.pawlowski.sportnite.presentation.view_models_related.sign_in_screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SignInScreenViewModel @Inject constructor(

): ISignInScreenViewModel, ViewModel() {
    override val container: Container<SignInScreenUiState, SignInScreenSideEffect> = container(
        SignInScreenUiState()
    )

    override fun changePhoneInput(newValue: String) = intent {
        reduce {
            state.copy(phoneNumberInput = newValue)
        }
    }

    override fun sendVerificationCodeClick() = intent {
        reduce {
            state.copy(isLoading = true)
        }
        reduce {
            state.copy(isLoading = false)
        }
        postSideEffect(SignInScreenSideEffect.NavigateToNextScreen)
    }
}