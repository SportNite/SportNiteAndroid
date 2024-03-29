package com.pawlowski.models.params_models

import com.pawlowski.models.Sport
import com.pawlowski.utils.UiDate

data class AddGameOfferParams(
    val date: UiDate,
    val placeOrAddress: String,
    val city: String,
    val additionalNotes: String,
    val sport: Sport,
)
