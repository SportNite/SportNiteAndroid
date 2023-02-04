package com.pawlowski.sportnite.presentation.view_models_related.sign_in_screen

import com.pawlowski.utils.UiText

sealed interface SignInScreenSideEffect
{
    object NavigateToNextScreen: SignInScreenSideEffect
    object NavigateToSignedInScreen: SignInScreenSideEffect

    data class DisplayErrorToast(val message: UiText): SignInScreenSideEffect
}