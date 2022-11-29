package com.pawlowski.sportnite.presentation.view_models_related.home_screen

import androidx.lifecycle.ViewModel
import com.pawlowski.sportnite.presentation.ui.utils.getMeetingForPreview
import com.pawlowski.sportnite.presentation.ui.utils.getMeetingsListForPreview
import com.pawlowski.sportnite.presentation.ui.utils.getUserForPreview
import com.pawlowski.sportnite.utils.UiData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(

): IHomeScreenViewModel, ViewModel() {
    override val container: Container<HomeScreenUiState, HomeScreenSideEffect> = container(initialState = HomeScreenUiState(
        upcomingMeetings = UiData.Success(true, getMeetingsListForPreview()),
        user = getUserForPreview()
    ))
}