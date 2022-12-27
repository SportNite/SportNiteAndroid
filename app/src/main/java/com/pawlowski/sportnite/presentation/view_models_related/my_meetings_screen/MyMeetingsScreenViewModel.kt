package com.pawlowski.sportnite.presentation.view_models_related.my_meetings_screen

import androidx.lifecycle.ViewModel
import com.pawlowski.sportnite.presentation.use_cases.GetIncomingMeetingsUseCase
import com.pawlowski.sportnite.presentation.use_cases.GetMyOffersUseCase
import com.pawlowski.sportnite.presentation.use_cases.GetOffersToAcceptUseCase
import com.pawlowski.sportnite.utils.UiData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MyMeetingsScreenViewModel @Inject constructor(
    private val getOffersToAcceptUseCase: GetOffersToAcceptUseCase,
    private val getIncomingMeetingsUseCase: GetIncomingMeetingsUseCase,
    private val getMyOffersUseCase: GetMyOffersUseCase,

): IMyMeetingsScreenViewModel, ViewModel() {
    override val container: Container<MyMeetingsScreenUiState, MyMeetingsScreenSideEffect> =
        container(
            initialState = MyMeetingsScreenUiState(
                UiData.Loading(),
                UiData.Loading(),
                UiData.Loading(),
                UiData.Loading()
            )
        )

    private fun observeOffersToAccept() = intent(registerIdling = false) {
        getOffersToAcceptUseCase(null).collectLatest {
            reduce {
                state.copy(offersToAccept = it)
            }
        }
    }

    private fun observeMyOffers() = intent(registerIdling = false) {
        getMyOffersUseCase().collectLatest {
            reduce {
                state.copy(myOffers = it)
            }
        }
    }

    private fun observeIncomingMeetings() = intent(registerIdling = false) {
        getIncomingMeetingsUseCase(null).collectLatest {
            reduce {
                state.copy(incomingMeetings = it)
            }
        }
    }

    init {
        observeIncomingMeetings()
        observeOffersToAccept()
        observeMyOffers()
    }
}