package com.pawlowski.sportnite.presentation.view_models_related.waiting_for_user_info_screen

sealed interface WaitingForUserInfoSideEffect {
    object NavigateToHomeScreen: WaitingForUserInfoSideEffect
    object NavigateToAccountDetailsScreen: WaitingForUserInfoSideEffect
    object NavigateToChooseSportsScreen: WaitingForUserInfoSideEffect
}