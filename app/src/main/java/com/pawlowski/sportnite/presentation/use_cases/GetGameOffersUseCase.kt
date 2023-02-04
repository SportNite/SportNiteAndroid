package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.sportnite.presentation.models.GameOffer
import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.utils.UiData
import kotlinx.coroutines.flow.Flow

fun interface GetGameOffersUseCase: (Sport?) -> Flow<UiData<List<GameOffer>>>