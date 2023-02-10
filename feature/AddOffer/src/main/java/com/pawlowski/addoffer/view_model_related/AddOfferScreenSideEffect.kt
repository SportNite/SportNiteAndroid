package com.pawlowski.addoffer.view_model_related

import com.pawlowski.utils.UiText

sealed interface AddOfferScreenSideEffect {
    data class ShowToast(val message: UiText): AddOfferScreenSideEffect
    data class ShowToastAndChangeScreen(val message: UiText): AddOfferScreenSideEffect
}