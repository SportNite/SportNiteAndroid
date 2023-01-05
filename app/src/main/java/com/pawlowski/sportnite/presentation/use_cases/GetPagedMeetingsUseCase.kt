package com.pawlowski.sportnite.presentation.use_cases

import androidx.paging.PagingData
import com.pawlowski.sportnite.presentation.models.Meeting
import kotlinx.coroutines.flow.Flow

fun interface GetPagedMeetingsUseCase: () -> Flow<PagingData<Meeting>>