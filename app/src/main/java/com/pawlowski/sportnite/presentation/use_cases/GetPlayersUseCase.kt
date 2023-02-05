package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.models.AdvanceLevel
import com.pawlowski.models.Player
import com.pawlowski.models.Sport
import com.pawlowski.utils.UiData
import kotlinx.coroutines.flow.Flow

fun interface GetPlayersUseCase: (Sport?, String?, AdvanceLevel?) -> Flow<UiData<List<Player>>>