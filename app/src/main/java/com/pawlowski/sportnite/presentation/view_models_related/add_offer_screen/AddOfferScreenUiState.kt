package com.pawlowski.sportnite.presentation.view_models_related.add_offer_screen

import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.sportnite.utils.UiDate

data class AddOfferScreenUiState(
    val meetingDateTime: UiDate? = null,
    val sport: Sport? = null,
    val cityInput: String = "",
    val placeOrAddressInput: String = "",
    val additionalNotesInput: String = "",
    val isLoading: Boolean = false
    )
