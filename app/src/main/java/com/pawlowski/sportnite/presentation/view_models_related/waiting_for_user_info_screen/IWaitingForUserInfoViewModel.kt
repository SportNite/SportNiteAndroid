package com.pawlowski.sportnite.presentation.view_models_related.waiting_for_user_info_screen

import org.orbitmvi.orbit.ContainerHost

interface IWaitingForUserInfoViewModel: ContainerHost<WaitingForUserInfoUiState, WaitingForUserInfoSideEffect> {

    fun checkUserInfo()
}