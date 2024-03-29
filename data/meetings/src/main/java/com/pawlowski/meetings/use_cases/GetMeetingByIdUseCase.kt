package com.pawlowski.meetings.use_cases

import com.pawlowski.models.Meeting
import com.pawlowski.utils.UiData
import kotlinx.coroutines.flow.Flow

fun interface GetMeetingByIdUseCase: (String) -> Flow<UiData<Meeting>>