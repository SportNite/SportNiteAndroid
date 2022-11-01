package com.pawlowski.sportnite.presentation.view_models_related.sign_in_screen

import org.orbitmvi.orbit.ContainerHost

interface ISignInScreenViewModel: ContainerHost<SignInScreenUiState, SignInScreenSideEffect> {
    fun changePhoneInput(newValue: String)
    fun sendVerificationCodeClick()
}