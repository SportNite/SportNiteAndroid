package com.pawlowski.waitingforuserinfo.view_model_related

sealed interface WaitingForUserInfoSideEffect {
    object NavigateToHomeScreen: WaitingForUserInfoSideEffect
    object NavigateToAccountDetailsScreen: WaitingForUserInfoSideEffect
    object NavigateToChooseSportsScreen: WaitingForUserInfoSideEffect
}