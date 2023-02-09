package com.pawlowski.sportnite.presentation.view_models_related.account_details_screen

import com.pawlowski.user.use_cases.UpdateUserInfoUseCase
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
        val sut = com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenViewModel(
            mockk()
        ).test(com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState())

        sut.testIntent { changeNameAndSurnameInput("newValue1") }
        sut.testIntent { changeNameAndSurnameInput("newValue12") }
        sut.testIntent { changeNameAndSurnameInput("newValue") }


        sut.assert(com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState()) {
            states(
                {
                    com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState(
                        nameAndSurnameInput = "newValue1"
                    )
                },
                {
                    com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState(
                        nameAndSurnameInput = "newValue12"
                    )
                },
                {
                    com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState(
                        nameAndSurnameInput = "newValue"
                    )
                },
            )
        }
    }

    @Test
    fun changeDateInput() = runTest {
        val sut = com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenViewModel(
            mockk()
        ).test(com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState())
        val newUiDate = UiDate(OffsetDateTime.parse("2023-01-15T11:00:00.000Z"))
        sut.testIntent { changeDateInput(newUiDate) }

        sut.assert(com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState()) {
            states(
                {
                    com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState(
                        dateOfBirthInput = newUiDate
                    )
                },
            )
        }
    }

    @Test
    fun changeIsMaleInput() = runTest {
        val sut = com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenViewModel(
            mockk()
        ).test(com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState())
        sut.testIntent { changeIsMaleInput(false) }
        sut.testIntent { changeIsMaleInput(true) }
        sut.testIntent { changeIsMaleInput(false) }


        sut.assert(com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState()) {
            states(
                {
                    com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState(
                        isMaleInput = false
                    )
                },
                {
                    com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState(
                        isMaleInput = true
                    )
                },
                {
                    com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState(
                        isMaleInput = false
                    )
                },
            )
        }
    }

    @Test
    fun changePhotoInput() = runTest {
        val sut = com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenViewModel(
            mockk()
        ).test(com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState())
        val newValue = "photo.png"
        sut.testIntent { changePhotoInput(newValue) }

        sut.assert(com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState()) {
            states(
                { com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState(photo = newValue) },
            )
        }
    }

    @Test
    fun changeTimeAvailabilityInput() = runTest {
        val sut = com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenViewModel(
            mockk()
        ).test(com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState())
        val newValue = "2h a day"
        sut.testIntent { changeTimeAvailabilityInput(newValue) }

        sut.assert(com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState()) {
            states(
                {
                    com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState(
                        timeAvailabilityInput = newValue
                    )
                },
            )
        }
    }

    @Test
    fun continueClick_missingValues_showToast() = runTest {
        val sut = com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenViewModel(
            mockk()
        ).test(
            com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState(

            )
        )
        sut.testIntent { continueClick() }

        sut.assert(com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState()) {
            postedSideEffects(com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenSideEffect.ShowToastMessage(someFieldsMissingText))
            states(
                {
                    com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState(
                        isLoading = true
                    )
                },
                {
                    com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState(
                        isLoading = false
                    )
                }
            )
        }
    }

    @Test
    fun continueClick_missingValues2_showToast() = runTest {
        val initialState =
            com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState(
                nameAndSurnameInput = "Wojciech Kowalski"
            )
        val sut = com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenViewModel(
            mockk()
        ).test(initialState)
        sut.testIntent { continueClick() }

        sut.assert(initialState) {
            postedSideEffects(com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenSideEffect.ShowToastMessage(someFieldsMissingText))
            states(
                { initialState.copy(isLoading = true) },
                { initialState.copy(isLoading = false) }
            )
        }
    }

    @Test
    fun continueClick_missingValues3_showToast() = runTest {
        val initialState =
            com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState(
                nameAndSurnameInput = "Wojciech Kowalski",
                dateOfBirthInput = UiDate(OffsetDateTime.parse("2003-01-15T11:00:00.000Z"))
            )

        val sut = com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenViewModel(
            mockk()
        ).test(initialState)
        sut.testIntent { continueClick() }

        sut.assert(initialState) {
            postedSideEffects(com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenSideEffect.ShowToastMessage(photoRequiredText))
            states(
                { initialState.copy(isLoading = true) },
                { initialState.copy(isLoading = false) }
            )
        }
    }

    @Test
    fun continueClick_missingValues4_showToast() = runTest {
        val initialState =
            com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState(
                nameAndSurnameInput = "Wojciech Kowalski",
                photo = "photo.png"
            )

        val sut = com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenViewModel(
            mockk()
        ).test(initialState)
        sut.testIntent { continueClick() }

        sut.assert(initialState) {
            states(
                { initialState.copy(isLoading = true) },
                { initialState.copy(isLoading = false) }
            )
            postedSideEffects(com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenSideEffect.ShowToastMessage(someFieldsMissingText))
        }
    }

    @Test
    fun continueClick_correctValuesAndSuccess_navigateToNextScreen() = runTest {
        val initialState =
            com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState(
                nameAndSurnameInput = "Wojciech Kowalski",
                photo = "photo.png",
                dateOfBirthInput = UiDate(OffsetDateTime.parse("2003-01-15T11:00:00.000Z"))

            )
        val updateProfileUseCaseMock = mockk<UpdateUserInfoUseCase>()
        val sut = com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenViewModel(
            updateUserInfoUseCase = updateProfileUseCaseMock
        ).test(initialState)

        coEvery {
            updateProfileUseCaseMock(any())
        } returns Resource.Success(Unit)

        sut.testIntent { continueClick() }

        sut.assert(initialState) {
            postedSideEffects(com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenSideEffect.NavigateToNextScreen)
            states(
                { initialState.copy(isLoading = true) },
                { initialState.copy(isLoading = false) }
            )
        }

        coVerify { updateProfileUseCaseMock(any()) }
    }

    @Test
    fun continueClick_correctValuesAndFailure_showToast() = runTest {
        val initialState =
            com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenUiState(
                nameAndSurnameInput = "Wojciech Kowalski",
                photo = "photo.png",
                dateOfBirthInput = UiDate(OffsetDateTime.parse("2003-01-15T11:00:00.000Z"))

            )
        val updateProfileUseCaseMock = mockk<UpdateUserInfoUseCase>()
        val sut = com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenViewModel(
            updateUserInfoUseCase = updateProfileUseCaseMock
        ).test(initialState)

        coEvery {
            updateProfileUseCaseMock(any())
        } returns Resource.Error(defaultRequestError)

        sut.testIntent { continueClick() }

        sut.assert(initialState) {
            postedSideEffects(com.pawlowski.accountdetails.view_model_related.AccountDetailsScreenSideEffect.ShowToastMessage(defaultRequestError))
            states(
                { initialState.copy(isLoading = true) },
                { initialState.copy(isLoading = false) }
            )
        }

        coVerify { updateProfileUseCaseMock(any()) }
    }


}