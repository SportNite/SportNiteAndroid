package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.sportnite.presentation.models.GameOfferToAccept
import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.sportnite.utils.UiData
import kotlinx.coroutines.flow.Flow

fun interface GetOffersToAcceptUseCase: (Sport?) -> Flow<UiData<List<GameOfferToAccept>>>