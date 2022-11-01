package com.pawlowski.sportnite.presentation.view_models_related.account_details_screen

import com.pawlowski.sportnite.utils.UiText

data class AccountDetailsScreenUiState(
    val nameAndSurnameInput: String = "",
    val nameAndSurnameInputError: UiText? = null,
    val isMaleInput: Boolean = true,
    val timeAvailabilityInput: String = "",
    val photo: String? = null,
    val isLoading: Boolean = false
)
