package com.pawlowski.addoffer.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pawlowski.sharedresources.R
import com.pawlowski.models.mappers.availableSports
import com.pawlowski.commonui.DateInputField
import com.pawlowski.commonui.SportInputField
import com.pawlowski.commonui.SportPickerDialog
import com.pawlowski.commonui.utils.showDateTimePicker
import com.pawlowski.addoffer.view_model_related.AddOfferScreenSideEffect
import com.pawlowski.addoffer.view_model_related.AddOfferScreenViewModel
import com.pawlowski.addoffer.view_model_related.IAddOfferScreenViewModel
import com.pawlowski.utils.UiDate
import kotlinx.coroutines.flow.collectLatest
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOfferScreen(
    viewModel: IAddOfferScreenViewModel = hiltViewModel<AddOfferScreenViewModel>(),
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collectLatest { event ->
            when (event) {
                is AddOfferScreenSideEffect.ShowToast -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
                is AddOfferScreenSideEffect.ShowToastAndChangeScreen -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                    onNavigateBack()
                }
            }
        }
    }

    val uiState = viewModel.container.stateFlow.collectAsState()
    val placeOrAddressState = remember {
        derivedStateOf {
            uiState.value.placeOrAddressInput
        }
    }

    val meetingDateTimeState = remember {
        derivedStateOf {
            uiState.value.meetingDateTime
        }
    }

    val cityInputState = remember {
        derivedStateOf {
            uiState.value.cityInput
        }
    }

    val additionalNotesState = remember {
        derivedStateOf {
            uiState.value.additionalNotesInput
        }
    }

    val sportState = remember {
        derivedStateOf {
            uiState.value.sport
        }
    }

    val isLoadingState = remember {
        derivedStateOf {
            uiState.value.isLoading
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = { onNavigateBack() }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_icon),
                        contentDescription = ""
                    )
                }
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Dodawanie oferty spotkania"
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            DateInputField(
                modifier = Modifier.padding(horizontal = 15.dp),
                onClick = {
                    showDateTimePicker(context, requireFutureDateTime = true) {
                        viewModel.changeDateTimeInput(UiDate(it))
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar_icon),
                        contentDescription = ""
                    )
                },
                dateText = meetingDateTimeState.value?.asLocalDateTimeString()
                    ?: "Kliknij aby wybrać czas spotkania"
            )
            Spacer(modifier = Modifier.height(15.dp))

            val isDialogVisible = remember {
                mutableStateOf(false)
            }
            SportInputField(
                modifier = Modifier.padding(horizontal = 15.dp),
                chosenSport = sportState.value,
                onClick = {
                    isDialogVisible.value = true
                }
            )

            SportPickerDialog(showDialog = { isDialogVisible.value },
                availableSports = remember {
                    availableSports.values.toList()
                }, onSportChosen = {
                    viewModel.changeSport(it)
                }, onDismissDialog = {
                    isDialogVisible.value = false
                })
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                value = cityInputState.value,
                onValueChange = {
                    viewModel.changeCityInput(it)
                },
                label = {
                    Text(text = "Miasto")
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.place_icon),
                        contentDescription = ""
                    )
                }
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                value = placeOrAddressState.value,
                onValueChange = {
                    viewModel.changePlaceOrAddressInput(it)
                },
                label = {
                    Text(text = "Miejsce lub adres")
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.place_icon),
                        contentDescription = ""
                    )
                }
            )

            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                value = additionalNotesState.value,
                onValueChange = {
                    viewModel.changeAdditionalNotesInput(it)
                },
                label = {
                    Text(text = "Dodatkowe informacje")
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.notes_icon),
                        contentDescription = ""
                    )
                }
            )

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                enabled = !isLoadingState.value,
                onClick = { viewModel.addOfferClick() }
            ) {
                Text(text = "Dodaj ofertę")
            }


        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddOfferScreenPreview() {
    AddOfferScreen()
}