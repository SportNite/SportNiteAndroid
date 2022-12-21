package com.pawlowski.sportnite.presentation.view_models_related.add_offer_screen

import com.pawlowski.sportnite.utils.UiText

sealed interface AddOfferScreenSideEffect {
    data class ShowToast(val message: UiText): AddOfferScreenSideEffect
    data class ShowToastAndChangeScreen(val message: UiText): AddOfferScreenSideEffect
}