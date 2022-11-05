package com.pawlowski.sportnite.presentation.view_models_related.enter_sign_in_code_screen

import com.pawlowski.sportnite.utils.UiText

sealed interface EnterSignInCodeSideEffect {
    data class ShowErrorToast(val message: UiText): EnterSignInCodeSideEffect
    object MoveToAccountDetailsScreen: EnterSignInCodeSideEffect
    object MoveToHomeScreen: EnterSignInCodeSideEffect
}