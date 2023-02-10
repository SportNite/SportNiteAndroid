package com.pawlowski.fullscreenlist.view_model_related

import com.pawlowski.utils.UiText

sealed interface FullScreenListSideEffect {
    data class ShowToastMessage(val message: UiText): FullScreenListSideEffect
}