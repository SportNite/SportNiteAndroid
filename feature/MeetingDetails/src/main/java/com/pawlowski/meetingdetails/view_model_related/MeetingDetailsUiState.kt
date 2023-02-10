package com.pawlowski.meetingdetails.view_model_related

import com.pawlowski.models.Meeting
import com.pawlowski.utils.UiData

data class MeetingDetailsUiState(
    val meeting: UiData<Meeting>
)
