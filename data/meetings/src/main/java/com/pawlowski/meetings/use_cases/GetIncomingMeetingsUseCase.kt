package com.pawlowski.meetings.use_cases

import com.pawlowski.models.Meeting
import com.pawlowski.models.Sport
import com.pawlowski.utils.UiData
import kotlinx.coroutines.flow.Flow

fun interface GetIncomingMeetingsUseCase: (Sport?) -> Flow<UiData<List<Meeting>>>