package com.pawlowski.sportnite.presentation.view_models_related.add_offer_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.apollographql.apollo3.ApolloClient
import com.pawlowski.sportnite.domain.models.AddGameOfferParams
import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.sportnite.presentation.ui.utils.getSportForPreview
import com.pawlowski.sportnite.presentation.use_cases.AddGameOfferUseCase
import com.pawlowski.sportnite.type.CreateOfferInput
import com.pawlowski.sportnite.type.SportType
import com.pawlowski.sportnite.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AddOfferScreenViewModel @Inject constructor(
    private val addGameOfferUseCase: AddGameOfferUseCase,
): IAddOfferScreenViewModel, ViewModel() {

    override val container: Container<AddOfferScreenUiState, AddOfferScreenSideEffect> = container(
        initialState = AddOfferScreenUiState(sport = getSportForPreview())
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
        val lastState = state
        if(!lastState.isLoading) {
            reduce {
                state.copy(isLoading = true)
            }
            if(
                lastState.meetingDateTime != null &&
                lastState.cityInput.isNotEmpty() &&
                lastState.placeOrAddressInput.isNotEmpty() &&
                lastState.sport != null
            ) {
                val params = AddGameOfferParams(
                    date = lastState.meetingDateTime,
                    placeOrAddress = lastState.placeOrAddressInput,
                    city = lastState.cityInput,
                    additionalNotes = lastState.additionalNotesInput,
                    sport = lastState.sport
                )

                when(val response = addGameOfferUseCase(params)) {
                    is Resource.Success -> {
                        postSideEffect(
                            AddOfferScreenSideEffect.ShowToastAndChangeScreen(offerSuccessText)
                        )
                    }
                    is Resource.Error -> {
                        postSideEffect(
                            AddOfferScreenSideEffect.ShowToast(response.message?: defaultRequestError)
                        )
                    }
                }
            }
            else
            {
                postSideEffect(
                    AddOfferScreenSideEffect.ShowToast(UiText.NonTranslatable("Some fields are empty!"))
                )
            }


            reduce {
                state.copy(isLoading = false)
            }
        }
        //val response = apolloClient.mutation(CreateOfferMutation(CreateOfferInput(dateTime = "2022-12-22 15", latitude = 10.5, longitude = 10.5, sport = SportType.TENNIS, placeId = 0))).execute()
        //Log.d("response", response.data?.createOffer?.offerId?.toString()?:"")
    }

}