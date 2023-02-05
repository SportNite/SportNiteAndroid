package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.models.GameOfferToAccept
import com.pawlowski.models.Sport
import com.pawlowski.utils.UiData
import kotlinx.coroutines.flow.Flow

fun interface GetOffersToAcceptUseCase: (Sport?) -> Flow<UiData<List<GameOfferToAccept>>>