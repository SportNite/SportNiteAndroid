package com.pawlowski.sportnite.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pawlowski.sportnite.presentation.mappers.asGameOffer
import com.pawlowski.sportnite.presentation.models.GameOffer
import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.sportnite.presentation.ui.reusable_components.*
import com.pawlowski.sportnite.presentation.ui.utils.OrbitMviPreviewViewModel
import com.pawlowski.sportnite.presentation.ui.utils.getSportForPreview
import com.pawlowski.sportnite.presentation.view_models_related.sport_screen.ISportScreenViewModel
import com.pawlowski.sportnite.presentation.view_models_related.sport_screen.SportScreenSideEffect
import com.pawlowski.sportnite.presentation.view_models_related.sport_screen.SportScreenUiState
import com.pawlowski.sportnite.presentation.view_models_related.sport_screen.SportScreenViewModel
import com.pawlowski.sportnite.utils.UiData
import com.pawlowski.sportnite.utils.dataOrNull
import com.pawlowski.sportnite.utils.isLoading
import org.orbitmvi.orbit.annotation.OrbitInternal

@Composable
fun SportScreen(
    modifier: Modifier = Modifier,
    viewModel: ISportScreenViewModel = hiltViewModel<SportScreenViewModel>(),
    onNavigateBack: () -> Unit = {},
    onNavigateToPlayerDetailsScreen: (String) -> Unit = {},
    onNavigateToMeetingDetailsScreen: (String) -> Unit = {},
    onNavigateToFindPlayersScreen: () -> Unit = {},
    onNavigateToFullScreenList: (String, Sport) -> Unit = { _, _ ->},
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { event ->
            when (event) {
                is SportScreenSideEffect.ShowToastMessage -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    val uiState = viewModel.container.stateFlow.collectAsState()
    val sportState = remember {
        derivedStateOf {
            uiState.value.sport
        }
    }

    val meetingsDataState = remember {
        derivedStateOf {
            uiState.value.myMeetings
        }
    }

    val meetingsValueState = remember {
        derivedStateOf {
            meetingsDataState.value.dataOrNull()?: listOf()
        }
    }

    val offersDataState = remember {
        derivedStateOf {
            uiState.value.gameOffers
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
            offersToAcceptDataState.value.dataOrNull()?: listOf()
        }
    }

    val playersDataState = remember {
        derivedStateOf {
            uiState.value.otherPlayers
        }
    }

    val playersValueState = remember {
        derivedStateOf {
            val value = playersDataState.value
            if (value is UiData.Success) {
                value.data
            } else {
                listOf()
            }
        }
    }

    Surface(modifier = modifier.fillMaxSize()) {

        val offersToAcceptMapped = remember(offersToAcceptValueState.value) {
            offersToAcceptValueState.value.map { it.asGameOffer() }
        }
        DisappearingSwipeRefresh(onRefresh = {
            viewModel.refresh()
        }) {
            LazyColumn(modifier = Modifier.fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally) {
                item {
                    SportHeader(
                        sport = sportState.value,
                        onBackClick = onNavigateBack
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = sportState.value.sportName.asString(), fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(5.dp))
                }

                gameOffersColumnItem(
                    offers = offersToAcceptMapped,
                    headerText = "Do akceptacji",
                    headersPadding = PaddingValues(horizontal = 10.dp),
                    onOfferTextButtonClick = {
                        viewModel.acceptOfferToAccept(it.offerUid)
                    },
                    onSeeMoreClick = {
                        onNavigateToFullScreenList("OffersToAccept", sportState.value)
                    },
                    isLoading = {
                        offersToAcceptDataState.value.isLoading()
                    },
                    displaySeeMore = false/*offersToAcceptDataState.value.isLoading() || !offersToAcceptDataState.value.dataOrNull().isNullOrEmpty()*/
                )
                item {
                    Spacer(modifier = Modifier.height(5.dp))
                }

                item {
                    IncomingMeetingsRow(
                        meetings = meetingsValueState.value,
                        headersPadding = PaddingValues(horizontal = 10.dp),
                        onMeetingCardClick = {
                            onNavigateToMeetingDetailsScreen(it.meetingUid)
                        },
                        isLoading = {
                            meetingsDataState.value.isLoading()
                        },
                        displaySeeMore = false
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(5.dp))
                }

                item {
                    PlayersRow(
                        players = playersValueState.value,
                        headersPadding = PaddingValues(horizontal = 10.dp),
                        onPlayerCardClick = {
                            onNavigateToPlayerDetailsScreen(it.uid)
                        },
                        onSeeMoreClick = {
                            onNavigateToFindPlayersScreen()
                        },
                        isLoading = {
                            playersDataState.value.isLoading()
                        },
                        displaySeeMore = playersDataState.value.isLoading() || !playersDataState.value.dataOrNull().isNullOrEmpty()
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(5.dp))
                }

                gameOffersColumnItem(
                    offers = offersValueState.value.take(5),
                    headerText = "Oferty na grę",
                    headersPadding = PaddingValues(horizontal = 10.dp),
                    onOfferTextButtonClick = {
                        if(it.myResponseIdIfExists != null) {
                            viewModel.deleteMyOfferToAccept(it.myResponseIdIfExists)
                        }
                        else
                        {
                            viewModel.sendGameOfferToAccept(it)
                        }
                    },
                    onSeeMoreClick = {
                        onNavigateToFullScreenList("Offers", sportState.value)
                    },
                    isLoading = {
                        offersDataState.value.isLoading()
                    },
                    displaySeeMore = offersDataState.value.isLoading() || !offersDataState.value.dataOrNull().isNullOrEmpty(),
                    offerTextButtonText = {
                        if(it.myResponseIdIfExists != null) {
                            Text(text = "Anuluj moją propozycję", color = Color.Red)
                        }
                        else
                        {
                            Text(text = "Akceptuj propozycję")
                        }
                    }
                )

                item {
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }

    }
}


@Composable
private fun SportHeader(
    sport: Sport,
    onBackClick: () -> Unit = {},
) {
    ScreenHeader(imageUrl = sport.sportBackgroundUrl, onBackClick = onBackClick)
}

@OrbitInternal
@Preview(showBackground = true)
@Composable
fun SportScreenPreview() {
    SportScreen(viewModel = object :
        OrbitMviPreviewViewModel<SportScreenUiState, SportScreenSideEffect>(),
        ISportScreenViewModel {
        override fun stateForPreview(): SportScreenUiState {
            return SportScreenUiState(
                sport = getSportForPreview(),

                )
        }

        override fun sendGameOfferToAccept(gameOffer: GameOffer) {}
        override fun acceptOfferToAccept(gameOfferToAcceptId: String) {}
        override fun refresh() {}
        override fun deleteMyOfferToAccept(offerToAcceptId: String) {}

    })
}