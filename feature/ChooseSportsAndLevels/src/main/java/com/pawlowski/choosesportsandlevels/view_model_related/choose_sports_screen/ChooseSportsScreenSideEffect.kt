package com.pawlowski.choosesportsandlevels.view_model_related.choose_sports_screen

import com.pawlowski.utils.UiText

internal sealed interface ChooseSportsScreenSideEffect {
    data class ShowToastMessage(val message: UiText): ChooseSportsScreenSideEffect
    object NavigateToChoseAdvanceLevelScreen: ChooseSportsScreenSideEffect
}