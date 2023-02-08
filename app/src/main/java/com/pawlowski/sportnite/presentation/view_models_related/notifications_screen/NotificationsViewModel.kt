package com.pawlowski.sportnite.presentation.view_models_related.notifications_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.pawlowski.sportnite.presentation.use_cases.GetPagedNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    getPagedNotificationsUseCase: GetPagedNotificationsUseCase
): INotificationsViewModel, ViewModel() {
    override val container: Container<NotificationsState, NotificationsSideEffect>
        = container(
            initialState = NotificationsState()
        )

    override val pagetNotifications by lazy {
        getPagedNotificationsUseCase()
            .cachedIn(viewModelScope)
    }
}