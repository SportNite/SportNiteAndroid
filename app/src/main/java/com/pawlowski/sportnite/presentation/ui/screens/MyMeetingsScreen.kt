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
import com.pawlowski.sportnite.presentation.ui.reusable_components.DisappearingSwipeRefresh
import com.pawlowski.sportnite.presentation.ui.reusable_components.IncomingMeetingsRow
import com.pawlowski.sportnite.presentation.ui.reusable_components.ScreenHeader
import com.pawlowski.sportnite.presentation.ui.reusable_components.gameOffersColumnItem
import com.pawlowski.sportnite.presentation.view_models_related.my_meetings_screen.IMyMeetingsScreenViewModel
import com.pawlowski.sportnite.presentation.view_models_related.my_meetings_screen.MyMeetingsScreenSideEffect
import com.pawlowski.sportnite.presentation.view_models_related.my_meetings_screen.MyMeetingsScreenViewModel
import com.pawlowski.sportnite.utils.dataOrNull
import com.pawlowski.sportnite.utils.isLoading

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
            meetingsDataState.value.dataOrNull()?: listOf()
        }
    }

    val offersDataState = remember {
        derivedStateOf {
            uiState.value.myOffers
        }
    }

    val offersValueState = remember {
        derivedStateOf {
            offersDataState.value.dataOrNull()?: listOf()
        }
    }

    val offersToAcceptDataState = remember {
        derivedStateOf {
            uiState.value.offersToAccept
        }
    }

    val offersToAcceptValueState = remember {
        derivedStateOf {
            offersToAcceptDataState.value.dataOrNull()?.map {
                it.asGameOffer()
            }?: listOf()
        }
    }

//    val historicalMeetingsDataState = remember {
//        derivedStateOf {
//            uiState.value.historicalMeetings
//        }
//    }

//    val historicalMeetingsValueState = remember {
//        derivedStateOf {
//            historicalMeetingsDataState.value.dataOrNull()?: listOf()
//        }
//    }

    Surface(modifier.fillMaxSize()) {
        DisappearingSwipeRefresh(onRefresh = {
            viewModel.refresh()
        }) {
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
                    displaySeeMore = false,
                    isLoading = {
                        offersToAcceptDataState.value.isLoading()
                    },
                    onOfferTextButtonClick = {
                        viewModel.acceptOfferToAccept(it.offerUid)
                    }
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
                        },
                        isLoading = {
                            meetingsDataState.value.isLoading()
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(5.dp))
                }

                gameOffersColumnItem(
                    offers = offersValueState.value,
                    headerText = "Moje oferty",
                    headersPadding = PaddingValues(horizontal = 10.dp),
                    displaySeeMore = false,
                    offerTextButtonText = {
                        Text(text = "Usuń ofertę", color = Color.Red)
                    },
                    onOfferTextButtonClick = {
                        viewModel.deleteOffer(it)
                    },
                    isLoading = {
                        offersDataState.value.isLoading()
                    }
                )

                item {
                    Spacer(modifier = Modifier.height(5.dp))
                }

                //TODO: Add history meetings
//            item {
//                IncomingMeetingsRow(
//                    meetings = historicalMeetingsValueState.value,
//                    headerText = "Historia",
//                    headersPadding = PaddingValues(horizontal = 10.dp),
//                    displaySeeMore = false,
//                    isLoading = { historicalMeetingsDataState.value.isLoading() }
//                )
//            }


            }
        }

    }
}



@Preview(showBackground = true)
@Composable
fun MyMeetingsScreenPreview() {
    MyMeetingsScreen()
}