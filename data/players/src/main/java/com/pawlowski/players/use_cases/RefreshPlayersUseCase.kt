package com.pawlowski.players.use_cases

import com.pawlowski.models.params_models.PlayersFilter
import com.pawlowski.utils.Resource

fun interface RefreshPlayersUseCase: suspend (PlayersFilter) -> Resource<Unit>