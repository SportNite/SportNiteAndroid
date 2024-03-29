package com.pawlowski.mymeetings.view_model_related

import androidx.lifecycle.ViewModel
import com.pawlowski.meetings.use_cases.GetIncomingMeetingsUseCase
import com.pawlowski.meetings.use_cases.RefreshMeetingsUseCase
import com.pawlowski.models.GameOffer
import com.pawlowski.models.params_models.MeetingsFilter
import com.pawlowski.models.params_models.OffersFilter
import com.pawlowski.repository.use_cases.DeleteMyOfferUseCase
import com.pawlowski.repository.use_cases.GetMyOffersUseCase
import com.pawlowski.repository.use_cases.RefreshOffersUseCase
import com.pawlowski.responses.use_cases.AcceptOfferToAcceptUseCase
import com.pawlowski.responses.use_cases.GetOffersToAcceptUseCase
import com.pawlowski.responses.use_cases.RefreshOffersToAcceptUseCase
import com.pawlowski.responses.use_cases.RejectOfferToAcceptUseCase
import com.pawlowski.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
internal class MyMeetingsScreenViewModel @Inject constructor(
    private val getOffersToAcceptUseCase: GetOffersToAcceptUseCase,
    private val getIncomingMeetingsUseCase: GetIncomingMeetingsUseCase,
    private val getMyOffersUseCase: GetMyOffersUseCase,
    private val deleteMyOfferUseCase: DeleteMyOfferUseCase,
    private val refreshOffersUseCase: RefreshOffersUseCase,
    private val refreshMeetingsUseCase: RefreshMeetingsUseCase,
    private val refreshOffersToAcceptUseCase: RefreshOffersToAcceptUseCase,
    private val acceptOfferToAcceptUseCase: AcceptOfferToAcceptUseCase,
    private val rejectOfferToAcceptUseCase: RejectOfferToAcceptUseCase,
    ): IMyMeetingsScreenViewModel, ViewModel() {
    override val container: Container<MyMeetingsScreenUiState, MyMeetingsScreenSideEffect> =
        container(
            initialState = MyMeetingsScreenUiState(
                UiData.Loading(),
                UiData.Loading(),
                UiData.Loading(),
                UiData.Loading()
            ),
            onCreate = {
                observeIncomingMeetings()
                observeOffersToAccept()
                observeMyOffers()
            }
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

    override fun deleteOffer(offer: GameOffer) = intent {
        //TODO: show loading
        val response = deleteMyOfferUseCase(offer.offerUid)
        response.onSuccess {
            postSideEffect(MyMeetingsScreenSideEffect.ShowToastMessage(offerDeletionSuccessText))
        }.onError { message, _ ->
            postSideEffect(MyMeetingsScreenSideEffect.ShowToastMessage(message))

        }

    }

    override fun refresh() = intent {
        refreshMeetingsUseCase(MeetingsFilter(null))
        refreshOffersUseCase(OffersFilter(null, myOffers = true))
        refreshOffersToAcceptUseCase(OffersFilter(sportFilter = null))
    }

    override fun acceptOfferToAccept(offerToAcceptUid: String) = intent {
        val response = acceptOfferToAcceptUseCase(offerToAcceptUid)
        response.onSuccess {
            postSideEffect(
                MyMeetingsScreenSideEffect.ShowToastMessage(
                    offerToAcceptAcceptSuccessText
                )
            )
        }.onError { message, _ ->
            postSideEffect(MyMeetingsScreenSideEffect.ShowToastMessage(message))

        }
    }

    override fun rejectOfferToAccept(offerToAcceptUid: String) = intent {
        val result = rejectOfferToAcceptUseCase(offerToAcceptUid)
        result.onSuccess {
            postSideEffect(MyMeetingsScreenSideEffect.ShowToastMessage(UiText.NonTranslatable("Oferta została pomyślnie odrzucona")))
        }.onError { message, _ ->
            postSideEffect(MyMeetingsScreenSideEffect.ShowToastMessage(message))
        }
    }


}