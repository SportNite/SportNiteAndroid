package com.pawlowski.sportnite.presentation.view_models_related.sign_in_screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SignInScreenViewModel @Inject constructor(): ISignInScreenViewModel, ViewModel() {
    override fun changePhoneInput(newValue: String) {
        TODO("Not yet implemented")
    }

    override fun sendVerificationCodeClick() {
        TODO("Not yet implemented")
    }

    override val container: Container<SignInScreenUiState, SignInScreenSideEffect> = container(
        SignInScreenUiState()
    )
}