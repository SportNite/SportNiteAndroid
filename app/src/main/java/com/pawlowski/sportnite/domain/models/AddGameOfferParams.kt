package com.pawlowski.sportnite.domain.models

import com.pawlowski.sportnite.presentation.models.Player
import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.sportnite.utils.UiDate

data class AddGameOfferParams(
    val owner: Player,
    val date: UiDate,
    val placeOrAddress: String,
    val city: String,
    val additionalNotes: String,
    val sport: Sport,
)
