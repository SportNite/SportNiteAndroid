package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.sportnite.domain.models.PlayersFilter
import com.pawlowski.utils.Resource

fun interface RefreshPlayersUseCase: suspend (PlayersFilter) -> Resource<Unit>