package com.pawlowski.authentication.view_model_related.sign_in_screen

import com.pawlowski.utils.UiText

internal sealed interface SignInScreenSideEffect
{
    object NavigateToNextScreen: SignInScreenSideEffect
    object NavigateToSignedInScreen: SignInScreenSideEffect

    data class DisplayErrorToast(val message: UiText): SignInScreenSideEffect
}