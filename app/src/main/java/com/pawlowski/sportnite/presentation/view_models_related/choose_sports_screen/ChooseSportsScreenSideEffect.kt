package com.pawlowski.sportnite.presentation.view_models_related.choose_sports_screen

import com.pawlowski.utils.UiText

sealed interface ChooseSportsScreenSideEffect {
    data class ShowToastMessage(val message: UiText): ChooseSportsScreenSideEffect
    object NavigateToChoseAdvanceLevelScreen: ChooseSportsScreenSideEffect
}