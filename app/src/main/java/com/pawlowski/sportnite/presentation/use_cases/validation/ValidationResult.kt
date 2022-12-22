package com.pawlowski.sportnite.presentation.use_cases.validation

import com.pawlowski.sportnite.utils.UiText

sealed class ValidationResult {
    object Success: ValidationResult()
    data class Error(val message: UiText): ValidationResult()
}