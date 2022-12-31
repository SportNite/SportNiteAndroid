package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.sportnite.presentation.models.Meeting
import com.pawlowski.sportnite.utils.UiData
import kotlinx.coroutines.flow.Flow

fun interface GetMeetingByIdUseCase: (String) -> Flow<UiData<Meeting>>