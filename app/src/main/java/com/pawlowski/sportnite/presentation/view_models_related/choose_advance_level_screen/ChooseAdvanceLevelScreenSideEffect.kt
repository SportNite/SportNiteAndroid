package com.pawlowski.sportnite.presentation.view_models_related.choose_advance_level_screen

import com.pawlowski.utils.UiText

sealed interface ChooseAdvanceLevelScreenSideEffect {
    object NavigateToHomeScreen: ChooseAdvanceLevelScreenSideEffect
    object NavigateToChooseSportsScreen: ChooseAdvanceLevelScreenSideEffect
    data class ShowToastMessage(val message: UiText): ChooseAdvanceLevelScreenSideEffect
}