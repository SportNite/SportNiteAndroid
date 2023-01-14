package com.pawlowski.sportnite.presentation.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.options
import com.pawlowski.sportnite.R
import com.pawlowski.sportnite.presentation.ui.reusable_components.DateInputField
import com.pawlowski.sportnite.presentation.ui.reusable_components.GenderInputField
import com.pawlowski.sportnite.presentation.ui.reusable_components.ProfilePictureWithEditIcon
import com.pawlowski.sportnite.presentation.ui.utils.OrbitMviPreviewViewModel
import com.pawlowski.sportnite.presentation.ui.utils.showDatePicker
import com.pawlowski.sportnite.presentation.view_models_related.account_details_screen.AccountDetailsScreenSideEffect
import com.pawlowski.sportnite.presentation.view_models_related.account_details_screen.AccountDetailsScreenUiState
import com.pawlowski.sportnite.presentation.view_models_related.account_details_screen.AccountDetailsScreenViewModel
import com.pawlowski.sportnite.presentation.view_models_related.account_details_screen.IAccountDetailsScreenViewModel
import com.pawlowski.sportnite.utils.UiDate
import org.orbitmvi.orbit.annotation.OrbitInternal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsScreen(
    viewModel: IAccountDetailsScreenViewModel = hiltViewModel<AccountDetailsScreenViewModel>(),
    onNavigateBack: () -> Unit = {},
    onNavigateToNextScreen: () -> Unit = {}
) {
    BackHandler {
        onNavigateBack()
    }
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

    val isLoadingState = remember {
        derivedStateOf {
            uiState.value.isLoading
        }
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { event ->
            when(event) {
                is AccountDetailsScreenSideEffect.NavigateToNextScreen -> {
                    onNavigateToNextScreen()
                }
                is AccountDetailsScreenSideEffect.ShowToastMessage -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
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
            val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
                if (result.isSuccessful) {
                    // use the cropped image
                    val uri = result.uriContent
                    uri?.let {
                        viewModel.changePhotoInput(it.toString())
                    }
                } else {
                    // an error occurred cropping
                    //result.error
                }
            }
            val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
                val cropOptions = options(uri = uri) {
                    setAspectRatio(1, 1)
                }
                imageCropLauncher.launch(cropOptions)
            }
            
            ProfilePictureWithEditIcon(
                modifier = Modifier
                    .align(CenterHorizontally)
                    .clickable {
                        imagePickerLauncher.launch("image/*")
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
                onClick = {
                    showDatePicker(context = context) {
                        viewModel.changeDateInput(UiDate(it))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                dateText = dateOfBirthInputState.value?.asLocalDateString()?:"Data urodzenia"
            )

            Spacer(modifier = Modifier.height(15.dp))
            val isGenderChooseDialogVisible = remember {
                mutableStateOf(false)
            }
            GenderInputField(
                onClick = {
                    isGenderChooseDialogVisible.value = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                isMale = isMaleInputState.value,
            )

            if(isGenderChooseDialogVisible.value)
            {
                AlertDialog(
                    onDismissRequest = {
                        isGenderChooseDialogVisible.value = false
                    },
                    text = {
                        Text(text = "Wybierz swoją płeć")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.changeIsMaleInput(true)
                                isGenderChooseDialogVisible.value = false

                            }
                        ) {
                            Text(text = "Mężczyzna")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                viewModel.changeIsMaleInput(false)
                                isGenderChooseDialogVisible.value = false

                            }
                        ) {
                            Text(text = "Kobieta")
                        }
                    }
                )
            }

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

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                onClick = { viewModel.continueClick() },
                enabled = !isLoadingState.value
            ) {
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
        override fun changeNameAndSurnameInput(newValue: String) {}
        override fun changeDateInput(newValue: UiDate) {}
        override fun changeIsMaleInput(newValue: Boolean) {}
        override fun changePhotoInput(newValue: String?) {}
        override fun changeTimeAvailabilityInput(newValue: String) {}
        override fun continueClick() {}
    })
}