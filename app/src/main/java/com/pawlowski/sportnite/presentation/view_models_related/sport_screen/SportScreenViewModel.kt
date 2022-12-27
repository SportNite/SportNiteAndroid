package com.pawlowski.sportnite.presentation.view_models_related.sport_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pawlowski.sportnite.presentation.models.GameOffer
import com.pawlowski.sportnite.presentation.models.GameOfferToAccept
import com.pawlowski.sportnite.presentation.ui.utils.getSportForPreview
import com.pawlowski.sportnite.presentation.use_cases.*
import com.pawlowski.sportnite.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SportScreenViewModel @Inject constructor(
    private val getIncomingMeetingsUseCase: GetIncomingMeetingsUseCase,
    private val getGameOffersUseCase: GetGameOffersUseCase,
    private val getOffersToAcceptUseCase: GetOffersToAcceptUseCase,
    private val sendGameOfferToAcceptUseCase: SendGameOfferToAcceptUseCase,
    private val getPlayersUseCase: GetPlayersUseCase,
    private val acceptOfferToAcceptUseCase: AcceptOfferToAcceptUseCase,
    savedStateHandle: SavedStateHandle
): ISportScreenViewModel, ViewModel() {
    private val currentSport = getSportForPreview() //TODO


    override fun sendGameOfferToAccept(gameOffer: GameOffer) = intent {
        val result = sendGameOfferToAcceptUseCase(gameOffer.offerUid)
        if(result is Resource.Success) {
            postSideEffect(SportScreenSideEffect.ShowToastMessage(message = offerToAcceptSuccessText))
        }
        else if(result is Resource.Error) {
            postSideEffect(SportScreenSideEffect.ShowToastMessage(message = defaultRequestError))
        }
    }

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

    private fun observePlayers() = intent(registerIdling = false) {
        repeatOnSubscription {
            getPlayersUseCase(null, null, null).collectLatest {
                reduce {
                    state.copy(otherPlayers = it)
                }
            }
        }
    }

    override fun acceptOfferToAccept(gameOfferToAcceptId: String) = intent {
        val result = acceptOfferToAcceptUseCase(gameOfferToAcceptId)
        if(result is Resource.Success) {
            postSideEffect(SportScreenSideEffect.ShowToastMessage(offerAcceptingSuccessText))
        }
        else if(result is Resource.Error) {
            postSideEffect(SportScreenSideEffect.ShowToastMessage(result.message?: defaultRequestError))
        }
    }

    init {
        observeGameOffers()
        observeOffersToAccept()
        observeIncomingMeetings()
        observePlayers()
    }
}