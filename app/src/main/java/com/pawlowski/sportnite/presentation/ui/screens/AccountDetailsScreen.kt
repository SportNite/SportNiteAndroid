package com.pawlowski.sportnite.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pawlowski.sportnite.R
import com.pawlowski.sportnite.presentation.ui.reusable_components.DateInputField
import com.pawlowski.sportnite.presentation.ui.reusable_components.GenderInputField
import com.pawlowski.sportnite.presentation.ui.reusable_components.ProfilePictureWithEditIcon
import com.pawlowski.sportnite.presentation.ui.utils.OrbitMviPreviewViewModel
import com.pawlowski.sportnite.presentation.view_models_related.account_details_screen.AccountDetailsScreenSideEffect
import com.pawlowski.sportnite.presentation.view_models_related.account_details_screen.AccountDetailsScreenUiState
import com.pawlowski.sportnite.presentation.view_models_related.account_details_screen.AccountDetailsScreenViewModel
import com.pawlowski.sportnite.presentation.view_models_related.account_details_screen.IAccountDetailsScreenViewModel
import org.orbitmvi.orbit.annotation.OrbitInternal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsScreen(
    viewModel: IAccountDetailsScreenViewModel = hiltViewModel<AccountDetailsScreenViewModel>(),
    onNavigateBack: () -> Unit = {},
    onNavigateToNextScreen: () -> Unit = {}
) {
    val uiState = viewModel.container.stateFlow.collectAsState()
    val photoUrlState = remember {
        derivedStateOf {
            uiState.value.photo
        }
    }
    val nameAndSurnameInputState = remember {
        derivedStateOf {
            uiState.value.nameAndSurnameInput
        }
    }

    val isMaleInputState = remember {
        derivedStateOf {
            uiState.value.isMaleInput
        }
    }

    val timeAvailabilityInputState = remember {
        derivedStateOf {
            uiState.value.timeAvailabilityInput
        }
    }

    val dateOfBirthInputState = remember {
        derivedStateOf {
            uiState.value.dateOfBirthInput
        }
    }

    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { event ->
            when(event) {
                is AccountDetailsScreenSideEffect.NavigateToNextScreen -> {
                    onNavigateToNextScreen()
                }
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            IconButton(onClick = { onNavigateBack() }) {
                Icon(painter = painterResource(id = R.drawable.back_icon), contentDescription = "")
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                text = "Uzupełnij swój profil",
                textAlign = TextAlign.Center,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                textAlign = TextAlign.Center,
                text = "Wygląda na to, że jesteś tu pierwszy raz.\nPodaj nam coś o sobie!",
            )
            Spacer(modifier = Modifier.height(30.dp))
            ProfilePictureWithEditIcon(
                modifier = Modifier
                    .align(CenterHorizontally)
                    .clickable {
                        //TODO()
                    },
                photoUrl = photoUrlState.value,
                size = 120.dp
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                value = nameAndSurnameInputState.value,
                onValueChange = { viewModel.changeNameAndSurnameInput(it) },
                label = { Text("Imię i nazwisko") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.person_icon),
                        contentDescription = "")
                }
            )

            Spacer(modifier = Modifier.height(15.dp))

            DateInputField(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                dateText = "Data urodzenia"
            )

            Spacer(modifier = Modifier.height(15.dp))

            GenderInputField(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                isMale = isMaleInputState.value,
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                value = timeAvailabilityInputState.value,
                onValueChange = { viewModel.changeTimeAvailabilityInput(it) },
                label = { Text("Dostępność czasowa") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.time_icon),
                        contentDescription = "")
                }
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                textAlign = TextAlign.Center,
                text = "Informacje będą widoczne dla innych użytkowników"
            )

            Spacer(modifier = Modifier.height(15.dp))

            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp), onClick = { viewModel.continueClick() }) {
                Text(text = "Kontynuuj")
            }
        }
    }
}

@OptIn(OrbitInternal::class)
@Preview(showBackground = true)
@Composable
fun AccountDetailsScreenPreview() {
    AccountDetailsScreen(viewModel = object : OrbitMviPreviewViewModel<AccountDetailsScreenUiState, AccountDetailsScreenSideEffect>(), IAccountDetailsScreenViewModel {
        override fun stateForPreview(): AccountDetailsScreenUiState {
            return AccountDetailsScreenUiState()
        }

        override fun changeNameAndSurnameInput(newValue: String) {
            TODO("Not yet implemented")
        }

        override fun changeDateInput(newValue: String) {
            TODO("Not yet implemented")
        }

        override fun changeIsMaleInput(newValue: Boolean) {
            TODO("Not yet implemented")
        }

        override fun changePhotoInput(newValue: String) {
            TODO("Not yet implemented")
        }

        override fun changeTimeAvailabilityInput(newValue: String) {
            TODO("Not yet implemented")
        }

        override fun continueClick() {
            TODO("Not yet implemented")
        }

        override fun backClick() {
            TODO("Not yet implemented")
        }

    })
}