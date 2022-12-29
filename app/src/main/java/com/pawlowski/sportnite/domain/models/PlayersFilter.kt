package com.pawlowski.sportnite.domain.models

import com.pawlowski.sportnite.presentation.models.AdvanceLevel
import com.pawlowski.sportnite.presentation.models.Sport

data class PlayersFilter(
    val sportFilter: Sport? = null,
    val nameSearch: String? = null,
    val level: AdvanceLevel? = null
)
