package com.pawlowski.accountdetails.view_model_related

import com.pawlowski.utils.UiDate
import com.pawlowski.utils.UiText

data class AccountDetailsScreenUiState(
    val nameAndSurnameInput: String = "",
    val nameAndSurnameInputError: UiText? = null,
    val dateOfBirthInput: UiDate? = null,
    val isMaleInput: Boolean = true,
    val timeAvailabilityInput: String = "",
    val photo: String? = null,
    val isLoading: Boolean = false
)
