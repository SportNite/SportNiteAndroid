package com.pawlowski.sportnite.presentation.view_models_related.account_details_screen

sealed interface AccountDetailsScreenSideEffect {
    object NavigateToNextScreen: AccountDetailsScreenSideEffect
}