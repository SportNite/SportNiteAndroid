package com.pawlowski.module_notifications.view_models_related

import androidx.paging.PagingData
import com.pawlowski.models.UserNotification
import kotlinx.coroutines.flow.Flow
import org.orbitmvi.orbit.ContainerHost

interface INotificationsViewModel: ContainerHost<NotificationsState, NotificationsSideEffect> {
    val pagedNotifications: Flow<PagingData<UserNotification>>
}