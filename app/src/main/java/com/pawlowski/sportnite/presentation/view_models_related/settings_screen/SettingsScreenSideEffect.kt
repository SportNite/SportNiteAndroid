package com.pawlowski.sportnite.presentation.view_models_related.settings_screen

sealed interface SettingsScreenSideEffect {
    object NavigateToLoginScreen: SettingsScreenSideEffect
}