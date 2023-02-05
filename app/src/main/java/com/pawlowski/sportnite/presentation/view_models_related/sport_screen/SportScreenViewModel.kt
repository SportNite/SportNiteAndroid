package com.pawlowski.sportnite.presentation.view_models_related.sport_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pawlowski.sportnite.data.mappers.getSportFromSportId
import com.pawlowski.sportnite.domain.models.MeetingsFilter
import com.pawlowski.sportnite.domain.models.OffersFilter
import com.pawlowski.sportnite.domain.models.PlayersFilter
import com.pawlowski.models.GameOffer
import com.pawlowski.sportnite.presentation.use_cases.*
import com.pawlowski.sportnite.utils.*
import com.pawlowski.utils.onAnyResourceHasError
import com.pawlowski.utils.onError
import com.pawlowski.utils.onSuccess
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
    private val refreshOffersToAcceptUseCase: RefreshOffersToAcceptUseCase,
    private val refreshOffersUseCase: RefreshOffersUseCase,
    private val refreshMeetingsUseCase: RefreshMeetingsUseCase,
    private val refreshPlayersUseCase: RefreshPlayersUseCase,
    private val deleteMyOfferToAcceptUseCase: DeleteMyOfferToAcceptUseCase,
    savedStateHandle: SavedStateHandle
): ISportScreenViewModel, ViewModel() {
    private val currentSport = getSportFromSportId(savedStateHandle.get<String>("sportId")!!)


    override val container: Container<SportScreenUiState, SportScreenSideEffect> = container(
        initialState = SportScreenUiState(currentSport),
        onCreate = {
            observeGameOffers()
            observeOffersToAccept()
            observeIncomingMeetings()
            observePlayers()
        }
    )

    override fun sendGameOfferToAccept(gameOffer: GameOffer) = intent {
        val result = sendGameOfferToAcceptUseCase(gameOffer.offerUid)
        result.onSuccess {
            postSideEffect(SportScreenSideEffect.ShowToastMessage(message = offerToAcceptSuccessText))
        }.onError { message, _ ->
            postSideEffect(SportScreenSideEffect.ShowToastMessage(message = message))
        }
    }

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
            getPlayersUseCase(currentSport, null, null).collectLatest {
                reduce {
                    state.copy(otherPlayers = it)
                }
            }
        }
    }

    override fun acceptOfferToAccept(gameOfferToAcceptId: String) = intent {
        val result = acceptOfferToAcceptUseCase(gameOfferToAcceptId)
        result.onSuccess {
            postSideEffect(SportScreenSideEffect.ShowToastMessage(offerAcceptingSuccessText))
        }.onError { message, _ ->
            postSideEffect(SportScreenSideEffect.ShowToastMessage(message))
        }
    }

    override fun refresh() = intent {
        onAnyResourceHasError(
            refreshMeetingsUseCase(MeetingsFilter(currentSport)),
            refreshOffersUseCase(OffersFilter(sportFilter = currentSport)),
            refreshOffersToAcceptUseCase(OffersFilter(sportFilter = currentSport)),
            refreshPlayersUseCase(PlayersFilter(sportFilter = currentSport))
        ) {
            postSideEffect(SportScreenSideEffect.ShowToastMessage(defaultRequestError))
        }

    }

    override fun deleteMyOfferToAccept(offerToAcceptId: String) = intent {
        val result = deleteMyOfferToAcceptUseCase(offerToAcceptId)
        result.onSuccess {
            postSideEffect(SportScreenSideEffect.ShowToastMessage(offerToAcceptDeletionSuccessText))
        }.onError { message, _ ->
            postSideEffect(SportScreenSideEffect.ShowToastMessage(message))
        }
    }


}