package com.pawlowski.sportnite.presentation.view_models_related.sport_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pawlowski.sportnite.presentation.ui.utils.getSportForPreview
import com.pawlowski.sportnite.presentation.use_cases.GetGameOffersUseCase
import com.pawlowski.sportnite.presentation.use_cases.GetIncomingMeetingsUseCase
import com.pawlowski.sportnite.presentation.use_cases.GetOffersToAcceptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SportScreenViewModel @Inject constructor(
    private val getIncomingMeetingsUseCase: GetIncomingMeetingsUseCase,
    private val getGameOffersUseCase: GetGameOffersUseCase,
    private val getOffersToAcceptUseCase: GetOffersToAcceptUseCase,
    savedStateHandle: SavedStateHandle
): ISportScreenViewModel, ViewModel() {
    private val currentSport = getSportForPreview() //TODO
    override val container: Container<SportScreenUiState, SportScreenSideEffect> = container(
        initialState = SportScreenUiState(currentSport)
    )

    private fun observeOffersToAccept() = intent(registerIdling = false) {
        repeatOnSubscription {
            getOffersToAcceptUseCase(currentSport).collectLatest {
                reduce {
                    state.copy(offersToAccept = it)
                }
            }
        }
    }

    private fun observeGameOffers() = intent(registerIdling = false) {
        repeatOnSubscription {
            getGameOffersUseCase(currentSport).collectLatest {
                reduce {
                    state.copy(gameOffers = it)
                }
            }
        }
    }

    private fun observeIncomingMeetings() = intent(registerIdling = false) {
        repeatOnSubscription {
            getIncomingMeetingsUseCase(currentSport).collectLatest {
                reduce {
                    state.copy(myMeetings = it)
                }
            }
        }
    }

    init {
        observeGameOffers()
        observeOffersToAccept()
        observeIncomingMeetings()
    }
}