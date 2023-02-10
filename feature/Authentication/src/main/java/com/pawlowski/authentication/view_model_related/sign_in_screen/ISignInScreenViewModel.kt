package com.pawlowski.authentication.view_model_related.sign_in_screen

import org.orbitmvi.orbit.ContainerHost

internal interface ISignInScreenViewModel: ContainerHost<SignInScreenUiState, SignInScreenSideEffect> {
    fun changePhoneInput(newValue: String)
    fun sendVerificationCodeClick()
}