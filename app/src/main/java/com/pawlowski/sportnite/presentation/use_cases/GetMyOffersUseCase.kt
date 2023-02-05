package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.models.GameOffer
import com.pawlowski.utils.UiData
import kotlinx.coroutines.flow.Flow

fun interface GetMyOffersUseCase: () -> Flow<UiData<List<GameOffer>>>