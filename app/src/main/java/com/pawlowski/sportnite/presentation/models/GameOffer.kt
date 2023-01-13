package com.pawlowski.sportnite.presentation.models

import com.pawlowski.sportnite.utils.UiDate

data class GameOffer(
    val owner: Player,
    val date: UiDate,
    val placeOrAddress: String,
    val city: String,
    val additionalNotes: String,
    val sport: Sport,
    val offerUid: String,
    val myResponseIdIfExists: String? = null,
)
