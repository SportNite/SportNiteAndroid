package com.pawlowski.sportnite.domain.models

import com.pawlowski.models.Sport

data class OffersFilter(
    val sportFilter: Sport?,
    val myOffers: Boolean = false
)
