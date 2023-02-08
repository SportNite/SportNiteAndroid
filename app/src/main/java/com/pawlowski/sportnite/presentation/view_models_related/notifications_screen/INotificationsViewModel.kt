package com.pawlowski.sportnite.presentation.view_models_related.notifications_screen

import androidx.paging.PagingData
import com.pawlowski.models.UserNotification
import kotlinx.coroutines.flow.Flow
import org.orbitmvi.orbit.ContainerHost

interface INotificationsViewModel: ContainerHost<NotificationsState, NotificationsSideEffect> {
    val pagetNotifications: Flow<PagingData<UserNotification>>
}