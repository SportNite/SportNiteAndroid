package com.pawlowski.repository.use_cases

import com.pawlowski.models.GameOffer
import com.pawlowski.models.Sport
import com.pawlowski.utils.UiData
import kotlinx.coroutines.flow.Flow

fun interface GetGameOffersUseCase: (Sport?) -> Flow<UiData<List<GameOffer>>>