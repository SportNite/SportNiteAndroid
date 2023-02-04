package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.sportnite.presentation.models.AdvanceLevel
import com.pawlowski.sportnite.presentation.models.Player
import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.utils.UiData
import kotlinx.coroutines.flow.Flow

fun interface GetPlayersUseCase: (Sport?, String?, AdvanceLevel?) -> Flow<UiData<List<Player>>>