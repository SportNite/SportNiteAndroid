package com.pawlowski.sportnite.presentation.view_models_related.home_screen

import androidx.lifecycle.ViewModel
import com.pawlowski.sportnite.domain.models.MeetingsFilter
import com.pawlowski.sportnite.presentation.ui.utils.getMeetingsListForPreview
import com.pawlowski.sportnite.presentation.use_cases.GetIncomingMeetingsUseCase
import com.pawlowski.sportnite.presentation.use_cases.GetInfoAboutMeUseCase
import com.pawlowski.sportnite.presentation.use_cases.GetUserSportsUseCase
import com.pawlowski.sportnite.presentation.use_cases.RefreshMeetingsUseCase
import com.pawlowski.sportnite.utils.UiData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getIncomingMeetingsUseCase: GetIncomingMeetingsUseCase,
    private val getInfoAboutMeUseCase: GetInfoAboutMeUseCase,
    private val getUserSportsUseCase: GetUserSportsUseCase,
    private val refreshMeetingsUseCase: RefreshMeetingsUseCase
): IHomeScreenViewModel, ViewModel() {
    override fun refreshData() = intent {
        refreshMeetingsUseCase(MeetingsFilter(null))
    }

    override val container: Container<HomeScreenUiState, HomeScreenSideEffect> = container(initialState = HomeScreenUiState(
        upcomingMeetings = UiData.Success(true, getMeetingsListForPreview()),
        user = null
    ))

    private fun observeIncomingMeetings() = intent(registerIdling = false) {
        repeatOnSubscription {
            getIncomingMeetingsUseCase(null).collectLatest {
                reduce {
                    state.copy(upcomingMeetings = it)
                }
            }
        }
    }

    private fun observeInfoAboutMe() = intent(registerIdling = false) {
        repeatOnSubscription {
            getInfoAboutMeUseCase().collectLatest {
                reduce {
                    state.copy(user = it)
                }
            }
        }
    }

    private fun observeUserSports() = intent {
        repeatOnSubscription {
            getUserSportsUseCase().collectLatest {
                reduce {
                    state.copy(userSports = it)
                }
            }
        }
    }

    init {
        observeUserSports()
        observeInfoAboutMe()
        observeIncomingMeetings()
    }
}