package com.pawlowski.sportnite.presentation.view_models_related.settings_screen

import org.orbitmvi.orbit.ContainerHost

interface ISettingsScreenViewModel: ContainerHost<SettingsScreenUiState, SettingsScreenSideEffect> {
    fun signOut()
}