package com.pawlowski.models.params_models

import com.pawlowski.models.AdvanceLevel
import com.pawlowski.models.Sport

data class PlayersFilter(
    val sportFilter: Sport? = null,
    val nameSearch: String? = null,
    val level: AdvanceLevel? = null
)
