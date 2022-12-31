package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.sportnite.presentation.models.PlayerDetails
import com.pawlowski.sportnite.utils.UiData
import kotlinx.coroutines.flow.Flow

fun interface GetPlayerDetailsUseCase: (String) -> Flow<UiData<PlayerDetails>>