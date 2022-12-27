package com.pawlowski.sportnite.presentation.view_models_related.my_meetings_screen

import androidx.lifecycle.ViewModel
import com.pawlowski.sportnite.presentation.models.GameOffer
import com.pawlowski.sportnite.presentation.use_cases.DeleteMyOfferUseCase
import com.pawlowski.sportnite.presentation.use_cases.GetIncomingMeetingsUseCase
import com.pawlowski.sportnite.presentation.use_cases.GetMyOffersUseCase
import com.pawlowski.sportnite.presentation.use_cases.GetOffersToAcceptUseCase
import com.pawlowski.sportnite.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MyMeetingsScreenViewModel @Inject constructor(
    private val getOffersToAcceptUseCase: GetOffersToAcceptUseCase,
    private val getIncomingMeetingsUseCase: GetIncomingMeetingsUseCase,
    private val getMyOffersUseCase: GetMyOffersUseCase,
    private val deleteMyOfferUseCase: DeleteMyOfferUseCase,

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

    override fun deleteOffer(offer: GameOffer) = intent {
        //TODO: show loading
        val response = deleteMyOfferUseCase(offer.offerUid)
        if(response is Resource.Success) {
            postSideEffect(MyMeetingsScreenSideEffect.ShowToastMessage(offerDeletionSuccessText))
        }
        else
        {
            postSideEffect(MyMeetingsScreenSideEffect.ShowToastMessage(response.message?: defaultRequestError))
        }
    }

    init {
        observeIncomingMeetings()
        observeOffersToAccept()
        observeMyOffers()
    }
}