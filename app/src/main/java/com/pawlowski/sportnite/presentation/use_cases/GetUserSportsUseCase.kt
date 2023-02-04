package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.utils.UiData
import kotlinx.coroutines.flow.Flow

fun interface GetUserSportsUseCase: () -> Flow<UiData<List<Sport>>>