package com.pawlowski.sportnite.presentation.view_models_related.enter_sign_in_code_screen

import org.orbitmvi.orbit.ContainerHost

interface IEnterSignInCodeScreenViewModel: ContainerHost<EnterSignInCodeUiState, EnterSignInCodeSideEffect> {
    fun changeCodeInput(newValue: String)
    fun sendVerificationCodeAgainClick()
    fun confirmCodeClick()
}