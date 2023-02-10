package com.pawlowski.sportnite.presentation.view_models_related.add_offer_screen

import com.pawlowski.addoffer.view_model_related.AddOfferScreenUiState
import com.pawlowski.addoffer.view_model_related.AddOfferScreenViewModel
import com.pawlowski.models.mappers.getSportFromSportId
import com.pawlowski.utils.UiDate
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.orbitmvi.orbit.test
import java.time.OffsetDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class AddOfferScreenViewModelTest {

    @Test
    fun changeDateTimeInput() = runTest {
        val sut = AddOfferScreenViewModel(
            mockk()
        ).test(AddOfferScreenUiState())

        val uiDate = UiDate(OffsetDateTime.now())
        sut.testIntent { changeDateTimeInput(uiDate) }

        sut.assert(AddOfferScreenUiState()) {
            states(
                { AddOfferScreenUiState(meetingDateTime = uiDate) },
            )
        }
    }

    @Test
    fun changeSport() = runTest {
        val sut = AddOfferScreenViewModel(
            mockk()
        ).test(AddOfferScreenUiState())

        val sportTennis = getSportFromSportId("TENNIS")
        val sportRun = getSportFromSportId("RUN")
        sut.testIntent { changeSport(sportTennis) }
        sut.testIntent { changeSport(sportRun) }

        sut.assert(AddOfferScreenUiState()) {
            states(
                { AddOfferScreenUiState(sport = sportTennis) },
                { AddOfferScreenUiState(sport = sportRun) },
            )
        }
    }

    @Test
    fun changeCityInput() = runTest {
        val sut = AddOfferScreenViewModel(
            mockk()
        ).test(AddOfferScreenUiState())

        sut.testIntent { changeCityInput("newValue") }

        sut.assert(AddOfferScreenUiState()) {
            states(
                { AddOfferScreenUiState(cityInput = "newValue") },
            )
        }
    }

    @Test
    fun changePlaceOrAddressInput() = runTest {
        val sut = AddOfferScreenViewModel(
            mockk()
        ).test(AddOfferScreenUiState())

        sut.testIntent { changePlaceOrAddressInput("newValue") }

        sut.assert(AddOfferScreenUiState()) {
            states(
                { AddOfferScreenUiState(placeOrAddressInput = "newValue") },
            )
        }
    }

    @Test
    fun changeAdditionalNotesInput() = runTest {
        val sut = AddOfferScreenViewModel(
            mockk()
        ).test(AddOfferScreenUiState())

        sut.testIntent { changeAdditionalNotesInput("newValue") }

        sut.assert(AddOfferScreenUiState()) {
            states(
                { AddOfferScreenUiState(additionalNotesInput = "newValue") },
            )
        }
    }

}