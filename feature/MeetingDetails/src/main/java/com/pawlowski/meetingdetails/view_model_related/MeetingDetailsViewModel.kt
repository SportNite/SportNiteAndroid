package com.pawlowski.meetingdetails.view_model_related

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pawlowski.meetings.use_cases.GetMeetingByIdUseCase
import com.pawlowski.utils.UiData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
internal class MeetingDetailsViewModel @Inject constructor(
    private val getMeetingByIdUseCase: GetMeetingByIdUseCase,
    savedStateHandle: SavedStateHandle
): IMeetingDetailsViewModel, ViewModel() {
    override val container: Container<MeetingDetailsUiState, MeetingDetailsSideEffect> =
        container(
            initialState = MeetingDetailsUiState(
                UiData.Loading()
            ),
            onCreate = {
                observeMeetingData()

            }
        )

    private val currentMeetingId = savedStateHandle.get<String>("meetingId")!!

    private fun observeMeetingData() = intent(registerIdling = false) {
        repeatOnSubscription {
            getMeetingByIdUseCase(currentMeetingId).collectLatest {
                reduce {
                    state.copy(
                        meeting = it
                    )
                }
            }
        }
    }


}