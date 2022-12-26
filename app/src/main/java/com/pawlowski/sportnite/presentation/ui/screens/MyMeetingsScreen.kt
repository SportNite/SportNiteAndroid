package com.pawlowski.sportnite.presentation.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawlowski.sportnite.presentation.mappers.asGameOffer
import com.pawlowski.sportnite.presentation.ui.reusable_components.IncomingMeetingsRow
import com.pawlowski.sportnite.presentation.ui.reusable_components.ScreenHeader
import com.pawlowski.sportnite.presentation.ui.reusable_components.gameOffersColumnItem
import com.pawlowski.sportnite.presentation.view_models_related.my_meetings_screen.MyMeetingsScreenUiState
import com.pawlowski.sportnite.utils.UiData

@Composable
fun MyMeetingsScreen() {

    val uiState = remember { //TODO get from view model
        mutableStateOf(MyMeetingsScreenUiState(
            UiData.Loading(),
            UiData.Loading(),
            UiData.Loading(),
            UiData.Loading()
        ))
    }

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

    Surface(Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                ScreenHeader(imageUrl = "", onBackClick = {

                })
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
                    displaySeeMore = false
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
                onOfferTextButtonClick = {
                    //TODO
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