package com.pawlowski.sportnite.presentation.view_models_related.enter_sign_in_code_screen

import com.pawlowski.sportnite.utils.UiText

data class EnterSignInCodeUiState(
    val codeInput: String = "",
    val codeInputError: UiText? = null,
    val isSendAgainAvailable: Boolean = false,
    val phoneNumber: String,
)
