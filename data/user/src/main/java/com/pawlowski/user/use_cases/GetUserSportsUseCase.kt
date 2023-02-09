package com.pawlowski.user.use_cases

import com.pawlowski.models.Sport
import com.pawlowski.utils.UiData
import kotlinx.coroutines.flow.Flow

fun interface GetUserSportsUseCase: () -> Flow<UiData<List<Sport>>>