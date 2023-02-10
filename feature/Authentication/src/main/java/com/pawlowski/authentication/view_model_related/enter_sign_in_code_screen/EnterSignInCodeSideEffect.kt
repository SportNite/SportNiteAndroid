package com.pawlowski.authentication.view_model_related.enter_sign_in_code_screen

import com.pawlowski.utils.UiText

internal sealed interface EnterSignInCodeSideEffect {
    data class ShowErrorToast(val message: UiText): EnterSignInCodeSideEffect
    object MoveToAccountDetailsScreen: EnterSignInCodeSideEffect
    object MoveToHomeScreen: EnterSignInCodeSideEffect
}