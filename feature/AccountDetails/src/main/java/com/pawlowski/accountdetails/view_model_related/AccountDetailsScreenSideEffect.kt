package com.pawlowski.accountdetails.view_model_related

import com.pawlowski.utils.UiText

sealed interface AccountDetailsScreenSideEffect {
    object NavigateToNextScreen: AccountDetailsScreenSideEffect
    data class ShowToastMessage(val message: UiText): AccountDetailsScreenSideEffect
}