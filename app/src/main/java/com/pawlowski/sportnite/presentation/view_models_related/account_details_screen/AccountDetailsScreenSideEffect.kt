package com.pawlowski.sportnite.presentation.view_models_related.account_details_screen

import com.pawlowski.utils.UiText

sealed interface AccountDetailsScreenSideEffect {
    object NavigateToNextScreen: AccountDetailsScreenSideEffect
    data class ShowToastMessage(val message: UiText): AccountDetailsScreenSideEffect
}