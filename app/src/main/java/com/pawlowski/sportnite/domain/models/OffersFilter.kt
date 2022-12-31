package com.pawlowski.sportnite.domain.models

import com.pawlowski.sportnite.presentation.models.Sport

data class OffersFilter(
    val sportFilter: Sport?,
    val myOffers: Boolean = false
)
