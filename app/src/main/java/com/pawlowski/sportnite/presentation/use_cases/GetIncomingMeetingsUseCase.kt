package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.sportnite.presentation.models.Meeting
import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.sportnite.utils.UiData
import kotlinx.coroutines.flow.Flow

fun interface GetIncomingMeetingsUseCase: (Sport?) -> Flow<UiData<List<Meeting>>>