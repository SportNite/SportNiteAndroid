package com.pawlowski.settings.view_model_related

import org.orbitmvi.orbit.ContainerHost

interface ISettingsScreenViewModel: ContainerHost<SettingsScreenUiState, SettingsScreenSideEffect> {
    fun signOut()
}