package com.pawlowski.module_notifications.view_models_related

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.pawlowski.notifications.use_cases.GetPagedNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
internal class NotificationsViewModel @Inject constructor(
    getPagedNotificationsUseCase: GetPagedNotificationsUseCase
): INotificationsViewModel, ViewModel() {
    override val container: Container<NotificationsState, NotificationsSideEffect>
        = container(
            initialState = NotificationsState()
        )

    override val pagedNotifications by lazy {
        getPagedNotificationsUseCase()
            .cachedIn(viewModelScope)
    }
}