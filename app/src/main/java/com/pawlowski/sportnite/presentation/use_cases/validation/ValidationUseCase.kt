package com.pawlowski.sportnite.presentation.use_cases.validation

interface ValidationUseCase {
    fun validate(value: String): ValidationResult
}