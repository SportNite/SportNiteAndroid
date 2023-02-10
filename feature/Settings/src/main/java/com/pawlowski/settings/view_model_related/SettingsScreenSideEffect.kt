package com.pawlowski.settings.view_model_related

sealed interface SettingsScreenSideEffect {
    object NavigateToLoginScreen: SettingsScreenSideEffect
}