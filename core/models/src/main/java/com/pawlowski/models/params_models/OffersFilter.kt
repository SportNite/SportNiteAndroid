package com.pawlowski.models.params_models

import com.pawlowski.models.Sport

data class OffersFilter(
    val sportFilter: Sport?,
    val myOffers: Boolean = false
)
