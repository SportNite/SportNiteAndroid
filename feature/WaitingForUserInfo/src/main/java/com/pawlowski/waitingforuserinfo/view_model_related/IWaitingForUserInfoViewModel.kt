package com.pawlowski.waitingforuserinfo.view_model_related

import org.orbitmvi.orbit.ContainerHost

interface IWaitingForUserInfoViewModel: ContainerHost<WaitingForUserInfoUiState, WaitingForUserInfoSideEffect> {

    fun checkUserInfo()
}