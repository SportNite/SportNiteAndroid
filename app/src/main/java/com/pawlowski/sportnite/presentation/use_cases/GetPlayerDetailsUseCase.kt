package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.models.PlayerDetails
import com.pawlowski.utils.UiData
import kotlinx.coroutines.flow.Flow

fun interface GetPlayerDetailsUseCase: (String) -> Flow<UiData<PlayerDetails>>