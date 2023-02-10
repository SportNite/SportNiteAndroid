package com.pawlowski.addoffer.view_model_related

import com.pawlowski.models.Sport
import com.pawlowski.utils.UiDate

data class AddOfferScreenUiState(
    val meetingDateTime: UiDate? = null,
    val sport: Sport? = null,
    val cityInput: String = "",
    val placeOrAddressInput: String = "",
    val additionalNotesInput: String = "",
    val isLoading: Boolean = false
    )
