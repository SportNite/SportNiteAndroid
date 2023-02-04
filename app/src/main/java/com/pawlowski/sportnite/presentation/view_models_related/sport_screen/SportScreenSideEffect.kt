package com.pawlowski.sportnite.presentation.view_models_related.sport_screen

import com.pawlowski.utils.UiText

sealed interface SportScreenSideEffect {
    data class ShowToastMessage(val message: UiText): SportScreenSideEffect
}