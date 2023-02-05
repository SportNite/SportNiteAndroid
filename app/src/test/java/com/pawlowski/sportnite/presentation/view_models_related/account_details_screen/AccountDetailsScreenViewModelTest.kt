package com.pawlowski.sportnite.presentation.view_models_related.account_details_screen

import com.pawlowski.sportnite.presentation.use_cases.UpdateUserInfoUseCase
import com.pawlowski.sportnite.utils.*
import com.pawlowski.utils.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.orbitmvi.orbit.test
import java.time.OffsetDateTime

@OptIn(ExperimentalCoroutinesApi::class)
internal class AccountDetailsScreenViewModelTest {


    @Before
    fun setUp() {

    }

    @Test
    fun changeNameAndSurnameInput() = runTest {
        val sut = AccountDetailsScreenViewModel(
            mockk()
        ).test(AccountDetailsScreenUiState())

        sut.testIntent { changeNameAndSurnameInput("newValue1") }
        sut.testIntent { changeNameAndSurnameInput("newValue12") }
        sut.testIntent { changeNameAndSurnameInput("newValue") }


        sut.assert(AccountDetailsScreenUiState()) {
            states(
                { AccountDetailsScreenUiState(nameAndSurnameInput = "newValue1") },
                { AccountDetailsScreenUiState(nameAndSurnameInput = "newValue12") },
                { AccountDetailsScreenUiState(nameAndSurnameInput = "newValue") },
            )
        }
    }

    @Test
    fun changeDateInput() = runTest {
        val sut = AccountDetailsScreenViewModel(
            mockk()
        ).test(AccountDetailsScreenUiState())
        val newUiDate = UiDate(OffsetDateTime.parse("2023-01-15T11:00:00.000Z"))
        sut.testIntent { changeDateInput(newUiDate) }

        sut.assert(AccountDetailsScreenUiState()) {
            states(
                { AccountDetailsScreenUiState(dateOfBirthInput = newUiDate) },
            )
        }
    }

    @Test
    fun changeIsMaleInput() = runTest {
        val sut = AccountDetailsScreenViewModel(
            mockk()
        ).test(AccountDetailsScreenUiState())
        sut.testIntent { changeIsMaleInput(false) }
        sut.testIntent { changeIsMaleInput(true) }
        sut.testIntent { changeIsMaleInput(false) }


        sut.assert(AccountDetailsScreenUiState()) {
            states(
                { AccountDetailsScreenUiState(isMaleInput = false) },
                { AccountDetailsScreenUiState(isMaleInput = true) },
                { AccountDetailsScreenUiState(isMaleInput = false) },
            )
        }
    }

    @Test
    fun changePhotoInput() = runTest {
        val sut = AccountDetailsScreenViewModel(
            mockk()
        ).test(AccountDetailsScreenUiState())
        val newValue = "photo.png"
        sut.testIntent { changePhotoInput(newValue) }

        sut.assert(AccountDetailsScreenUiState()) {
            states(
                { AccountDetailsScreenUiState(photo = newValue) },
            )
        }
    }

    @Test
    fun changeTimeAvailabilityInput() = runTest {
        val sut = AccountDetailsScreenViewModel(
            mockk()
        ).test(AccountDetailsScreenUiState())
        val newValue = "2h a day"
        sut.testIntent { changeTimeAvailabilityInput(newValue) }

        sut.assert(AccountDetailsScreenUiState()) {
            states(
                { AccountDetailsScreenUiState(timeAvailabilityInput = newValue) },
            )
        }
    }

    @Test
    fun continueClick_missingValues_showToast() = runTest {
        val sut = AccountDetailsScreenViewModel(
            mockk()
        ).test(AccountDetailsScreenUiState(

        ))
        sut.testIntent { continueClick() }

        sut.assert(AccountDetailsScreenUiState()) {
            postedSideEffects(AccountDetailsScreenSideEffect.ShowToastMessage(someFieldsMissingText))
            states(
                { AccountDetailsScreenUiState(isLoading = true) },
                { AccountDetailsScreenUiState(isLoading = false) }
            )
        }
    }

    @Test
    fun continueClick_missingValues2_showToast() = runTest {
        val initialState = AccountDetailsScreenUiState(
            nameAndSurnameInput = "Wojciech Kowalski"
        )
        val sut = AccountDetailsScreenViewModel(
            mockk()
        ).test(initialState)
        sut.testIntent { continueClick() }

        sut.assert(initialState) {
            postedSideEffects(AccountDetailsScreenSideEffect.ShowToastMessage(someFieldsMissingText))
            states(
                { initialState.copy(isLoading = true) },
                { initialState.copy(isLoading = false) }
            )
        }
    }

    @Test
    fun continueClick_missingValues3_showToast() = runTest {
        val initialState = AccountDetailsScreenUiState(
            nameAndSurnameInput = "Wojciech Kowalski",
            dateOfBirthInput = UiDate(OffsetDateTime.parse("2003-01-15T11:00:00.000Z"))
        )

        val sut = AccountDetailsScreenViewModel(
            mockk()
        ).test(initialState)
        sut.testIntent { continueClick() }

        sut.assert(initialState) {
            postedSideEffects(AccountDetailsScreenSideEffect.ShowToastMessage(photoRequiredText))
            states(
                { initialState.copy(isLoading = true) },
                { initialState.copy(isLoading = false) }
            )
        }
    }

    @Test
    fun continueClick_missingValues4_showToast() = runTest {
        val initialState = AccountDetailsScreenUiState(
            nameAndSurnameInput = "Wojciech Kowalski",
            photo = "photo.png"
        )

        val sut = AccountDetailsScreenViewModel(
            mockk()
        ).test(initialState)
        sut.testIntent { continueClick() }

        sut.assert(initialState) {
            states(
                { initialState.copy(isLoading = true) },
                { initialState.copy(isLoading = false) }
            )
            postedSideEffects(AccountDetailsScreenSideEffect.ShowToastMessage(someFieldsMissingText))
        }
    }

    @Test
    fun continueClick_correctValuesAndSuccess_navigateToNextScreen() = runTest {
        val initialState = AccountDetailsScreenUiState(
            nameAndSurnameInput = "Wojciech Kowalski",
            photo = "photo.png",
            dateOfBirthInput = UiDate(OffsetDateTime.parse("2003-01-15T11:00:00.000Z"))

        )
        val updateProfileUseCaseMock = mockk<UpdateUserInfoUseCase>()
        val sut = AccountDetailsScreenViewModel(
            updateUserInfoUseCase = updateProfileUseCaseMock
        ).test(initialState)

        coEvery {
            updateProfileUseCaseMock(any())
        } returns Resource.Success(Unit)

        sut.testIntent { continueClick() }

        sut.assert(initialState) {
            postedSideEffects(AccountDetailsScreenSideEffect.NavigateToNextScreen)
            states(
                { initialState.copy(isLoading = true) },
                { initialState.copy(isLoading = false) }
            )
        }

        coVerify { updateProfileUseCaseMock(any()) }
    }

    @Test
    fun continueClick_correctValuesAndFailure_showToast() = runTest {
        val initialState = AccountDetailsScreenUiState(
            nameAndSurnameInput = "Wojciech Kowalski",
            photo = "photo.png",
            dateOfBirthInput = UiDate(OffsetDateTime.parse("2003-01-15T11:00:00.000Z"))

        )
        val updateProfileUseCaseMock = mockk<UpdateUserInfoUseCase>()
        val sut = AccountDetailsScreenViewModel(
            updateUserInfoUseCase = updateProfileUseCaseMock
        ).test(initialState)

        coEvery {
            updateProfileUseCaseMock(any())
        } returns Resource.Error(defaultRequestError)

        sut.testIntent { continueClick() }

        sut.assert(initialState) {
            postedSideEffects(AccountDetailsScreenSideEffect.ShowToastMessage(defaultRequestError))
            states(
                { initialState.copy(isLoading = true) },
                { initialState.copy(isLoading = false) }
            )
        }

        coVerify { updateProfileUseCaseMock(any()) }
    }


}