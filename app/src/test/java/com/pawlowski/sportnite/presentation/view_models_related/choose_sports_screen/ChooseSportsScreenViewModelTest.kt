package com.pawlowski.sportnite.presentation.view_models_related.choose_sports_screen

import com.pawlowski.sportnite.data.mappers.getSportFromSportId
import com.pawlowski.sportnite.presentation.view_models_related.add_offer_screen.AddOfferScreenUiState
import com.pawlowski.sportnite.presentation.view_models_related.add_offer_screen.AddOfferScreenViewModel
import com.pawlowski.sportnite.utils.UiDate
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.orbitmvi.orbit.test
import java.time.OffsetDateTime


@OptIn(ExperimentalCoroutinesApi::class)
class ChooseSportsScreenViewModelTest {

    @Test
    fun changeDateTimeInput() = runTest {
        val initialState = ChooseSportsScreenUiState(mockk(), mockk())

        val sut = ChooseSportsScreenViewModel(
            mockk()
        ).test(initialState)

        sut.testIntent { changeSelectionOfSport(getSportFromSportId("RUN")) }
//        initialState.sports[getSportFromSportId("RUN")] = true

        sut.assert(initialState)
    }

}