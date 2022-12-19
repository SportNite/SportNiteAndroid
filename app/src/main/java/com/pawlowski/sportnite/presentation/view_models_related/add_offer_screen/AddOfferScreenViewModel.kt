package com.pawlowski.sportnite.presentation.view_models_related.add_offer_screen

import androidx.lifecycle.ViewModel
import com.apollographql.apollo3.ApolloClient
import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.sportnite.utils.UiDate
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AddOfferScreenViewModel @Inject constructor(

): IAddOfferScreenViewModel, ViewModel() {

    override val container: Container<AddOfferScreenUiState, AddOfferScreenSideEffect> = container(
        initialState = AddOfferScreenUiState()
    )

    override fun changeDateTimeInput(newValue: UiDate) = intent {
        reduce {
            state.copy(meetingDateTime = newValue)
        }
    }

    override fun changeSport(newValue: Sport) = intent {
        reduce {
            state.copy(sport = newValue)
        }
    }

    override fun changeCityInput(newValue: String) = intent {
        reduce {
            state.copy(cityInput = newValue)
        }
    }

    override fun changePlaceOrAddressInput(newValue: String) = intent {
        reduce {
            state.copy(placeOrAddressInput = newValue)
        }
    }

    override fun changeAdditionalNotesInput(newValue: String) = intent {
        reduce {
            state.copy(additionalNotesInput = newValue)
        }
    }

    override fun navigateBackClick() {
        TODO("Not yet implemented")
    }

    override fun addOfferClick() = intent {
        //val response = apolloClient.mutation(CreateOfferMutation(CreateOfferInput(dateTime = "2022-12-22 15", latitude = 10.5, longitude = 10.5, sport = SportType.TENNIS, placeId = 0))).execute()
        //Log.d("response", response.data?.createOffer?.offerId?.toString()?:"")
    }

}