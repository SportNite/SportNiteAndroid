package com.pawlowski.authentication.view_model_related.enter_sign_in_code_screen

import com.pawlowski.utils.UiText

data class EnterSignInCodeUiState(
    val codeInput: String = "",
    val codeInputError: UiText? = null,
    val isSendAgainAvailable: Boolean = false,
    val phoneNumber: String,
)
