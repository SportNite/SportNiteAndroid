package com.pawlowski.sportnite.presentation.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pawlowski.sportnite.presentation.mappers.asGameOffer
import com.pawlowski.sportnite.presentation.ui.reusable_components.IncomingMeetingsRow
import com.pawlowski.sportnite.presentation.ui.reusable_components.ScreenHeader
import com.pawlowski.sportnite.presentation.ui.reusable_components.gameOffersColumnItem
import com.pawlowski.sportnite.presentation.view_models_related.my_meetings_screen.IMyMeetingsScreenViewModel
import com.pawlowski.sportnite.presentation.view_models_related.my_meetings_screen.MyMeetingsScreenSideEffect
import com.pawlowski.sportnite.presentation.view_models_related.my_meetings_screen.MyMeetingsScreenViewModel
import com.pawlowski.sportnite.utils.UiData

@Composable
fun MyMeetingsScreen(
    modifier: Modifier = Modifier,
    viewModel: IMyMeetingsScreenViewModel = hiltViewModel<MyMeetingsScreenViewModel>(),
    onNavigateToHomeScreen: () -> Unit = {},
    onNavigateToMeetingDetailsScreen: (String) -> Unit = {}
) {

    BackHandler {
        onNavigateToHomeScreen()
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { event ->
            when(event) {
                is MyMeetingsScreenSideEffect.ShowToastMessage -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    val uiState = viewModel.container.stateFlow.collectAsState()

    val meetingsDataState = remember {
        derivedStateOf {
            uiState.value.incomingMeetings
        }
    }

    val meetingsValueState = remember {
        derivedStateOf {
            val meetingsDataValue = meetingsDataState.value
            if(meetingsDataValue is UiData.Success) {
                meetingsDataValue.data
            }
            else
                listOf()
        }
    }

    val offersDataState = remember {
        derivedStateOf {
            uiState.value.myOffers
        }
    }

    val offersValueState = remember {
        derivedStateOf {
            val offersValue = offersDataState.value
            if(offersValue is UiData.Success)
            {
                offersValue.data
            }
            else
                listOf()
        }
    }

    val offersToAcceptDataState = remember {
        derivedStateOf {
            uiState.value.offersToAccept
        }
    }

    val offersToAcceptValueState = remember {
        derivedStateOf {
            val offersToAcceptValue = offersToAcceptDataState.value
            if(offersToAcceptValue is UiData.Success)
            {
                offersToAcceptValue.data.map { it.asGameOffer() }
            }
            else
                listOf()
        }
    }

    val historicalMeetingsDataState = remember {
        derivedStateOf {
            uiState.value.historicalMeetings
        }
    }

    val historicalMeetingsValueState = remember {
        derivedStateOf {
            val historicalMeetingsValue = historicalMeetingsDataState.value
            if(historicalMeetingsValue is UiData.Success)
            {
                historicalMeetingsValue.data
            }
            else
                listOf()
        }
    }

    Surface(modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                ScreenHeader(
                    imageUrl = "https://images.unsplash.com/photo-1435527173128-983b87201f4d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2334&q=80",
                    onBackClick = {
                        onNavigateToHomeScreen()
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "Moje spotkania", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(5.dp))
            }

            gameOffersColumnItem(
                offers = offersToAcceptValueState.value,
                headerText = "Do akceptacji",
                headersPadding = PaddingValues(horizontal = 10.dp),
                displaySeeMore = false
            )
            item {
                Spacer(modifier = Modifier.height(5.dp))
            }

            item {
                IncomingMeetingsRow(
                    meetings = meetingsValueState.value,
                    headersPadding = PaddingValues(horizontal = 10.dp),
                    displaySeeMore = false,
                    onMeetingCardClick = {
                        onNavigateToMeetingDetailsScreen(it.meetingUid)
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(5.dp))
            }

            gameOffersColumnItem(
                offers = offersValueState.value.take(4),
                headerText = "Moje oferty",
                headersPadding = PaddingValues(horizontal = 10.dp),
                displaySeeMore = false,
                offerTextButtonText = {
                    Text(text = "Usuń ofertę", color = Color.Red)
                },
                onOfferTextButtonClick = {
                    viewModel.deleteOffer(it)
                }
            )

            item {
                Spacer(modifier = Modifier.height(5.dp))
            }

            item {
                IncomingMeetingsRow(
                    meetings = historicalMeetingsValueState.value,
                    headerText = "Historia",
                    headersPadding = PaddingValues(horizontal = 10.dp),
                    displaySeeMore = false
                )
            }


        }
    }
}



@Preview(showBackground = true)
@Composable
fun MyMeetingsScreenPreview() {
    MyMeetingsScreen()
}