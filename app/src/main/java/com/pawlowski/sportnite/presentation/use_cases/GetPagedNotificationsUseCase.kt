package com.pawlowski.sportnite.presentation.use_cases

import androidx.paging.PagingData
import com.pawlowski.models.UserNotification
import kotlinx.coroutines.flow.Flow

fun interface GetPagedNotificationsUseCase: () -> Flow<PagingData<UserNotification>>