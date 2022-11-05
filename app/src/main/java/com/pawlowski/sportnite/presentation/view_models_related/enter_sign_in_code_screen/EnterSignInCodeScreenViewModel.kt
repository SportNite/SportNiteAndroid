package com.pawlowski.sportnite.presentation.view_models_related.enter_sign_in_code_screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class EnterSignInCodeScreenViewModel @Inject constructor(

): IEnterSignInCodeScreenViewModel, ViewModel() {
    override val container: Container<EnterSignInCodeUiState, EnterSignInCodeSideEffect> = container(
        EnterSignInCodeUiState(phoneNumber = "33333333")
    )

    override fun changeCodeInput(newValue: String) {
        TODO("Not yet implemented")
    }

    override fun sendVerificationCodeAgainClick() {
        TODO("Not yet implemented")
    }

    override fun confirmCodeClick() {
        TODO("Not yet implemented")
    }
}