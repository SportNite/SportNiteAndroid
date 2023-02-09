package com.pawlowski.notifications

import androidx.paging.PagingData
import com.pawlowski.models.UserNotification
import kotlinx.coroutines.flow.Flow

interface INotificationsRepository {
    fun getPagedNotifications(): Flow<PagingData<UserNotification>>
}