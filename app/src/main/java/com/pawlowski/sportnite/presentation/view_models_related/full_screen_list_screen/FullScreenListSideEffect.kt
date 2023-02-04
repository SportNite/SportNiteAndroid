package com.pawlowski.sportnite.presentation.view_models_related.full_screen_list_screen

import com.pawlowski.utils.UiText

sealed interface FullScreenListSideEffect {
    data class ShowToastMessage(val message: UiText): FullScreenListSideEffect
}