package com.pawlowski.sportnite.presentation.view_models_related.meeting_details

import androidx.lifecycle.ViewModel
import com.pawlowski.sportnite.presentation.ui.utils.getMeetingForPreview
import com.pawlowski.sportnite.utils.UiData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MeetingDetailsViewModel @Inject constructor(

): IMeetingDetailsViewModel, ViewModel() {
    override val container: Container<MeetingDetailsUiState, MeetingDetailsSideEffect> =
        container(
            initialState = MeetingDetailsUiState(
                UiData.Success(true, getMeetingForPreview())
            )
        )
}