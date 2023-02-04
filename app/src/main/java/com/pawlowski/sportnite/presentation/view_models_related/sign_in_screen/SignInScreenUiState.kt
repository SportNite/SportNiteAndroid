package com.pawlowski.sportnite.presentation.view_models_related.sign_in_screen

import com.pawlowski.utils.UiText

data class SignInScreenUiState(
    val phoneNumberInput: String = "",
    val phoneNumberInputError: UiText? = null,
    val isLoading: Boolean = false,
)
