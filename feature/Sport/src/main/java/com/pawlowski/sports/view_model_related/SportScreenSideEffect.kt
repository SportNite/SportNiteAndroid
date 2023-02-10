package com.pawlowski.sports.view_model_related

import com.pawlowski.utils.UiText

sealed interface SportScreenSideEffect {
    data class ShowToastMessage(val message: UiText): SportScreenSideEffect
}