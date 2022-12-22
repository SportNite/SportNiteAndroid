package com.pawlowski.sportnite.presentation.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pawlowski.sportnite.presentation.models.GameOffer
import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.sportnite.presentation.ui.reusable_components.GameOfferCard
import com.pawlowski.sportnite.presentation.ui.utils.OrbitMviPreviewViewModel
import com.pawlowski.sportnite.presentation.ui.utils.getGameOfferListForPreview
import com.pawlowski.sportnite.presentation.ui.utils.getMeetingsListForPreview
import com.pawlowski.sportnite.presentation.ui.utils.getSportForPreview
import com.pawlowski.sportnite.presentation.view_models_related.sport_screen.ISportScreenViewModel
import com.pawlowski.sportnite.presentation.view_models_related.sport_screen.SportScreenSideEffect
import com.pawlowski.sportnite.presentation.view_models_related.sport_screen.SportScreenUiState
import com.pawlowski.sportnite.presentation.view_models_related.sport_screen.SportScreenViewModel
import com.pawlowski.sportnite.utils.UiData
import org.orbitmvi.orbit.annotation.OrbitInternal

@Composable
fun SportScreen(
    viewModel: ISportScreenViewModel = hiltViewModel<SportScreenViewModel>(),
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.container.stateFlow.collectAsState()
    val sportState = remember {
        derivedStateOf {
            uiState.value.sport
        }
    }

    val offersDataState = remember {
        derivedStateOf {
            uiState.value.gameOffers
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

    Surface(modifier = modifier.fillMaxSize()) {
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                SportHeader(sport = sportState.value)
            }
            item {
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = sportState.value.sportName.asString(), fontSize = 20.sp)
                Spacer(modifier = Modifier.height(5.dp))
            }
            gameOffersColumnItem(
                offers = getGameOfferListForPreview().take(3),
                headerText = "Do akceptacji",
                headersPadding = PaddingValues(horizontal = 10.dp)
            )
            item {
                Spacer(modifier = Modifier.height(5.dp))
            }

            item {
                IncomingMeetingsRow(
                    meetings = getMeetingsListForPreview(),
                    headersPadding = PaddingValues(horizontal = 10.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(5.dp))
            }

            gameOffersColumnItem(
                offers = offersValueState.value.take(4),
                headerText = "Oferty na grę",
                headersPadding = PaddingValues(horizontal = 10.dp)
            )

            item {
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}

fun LazyListScope.gameOffersColumnItem(
    offers: List<GameOffer>,
    headerText: String = "Oferty na grę",
    headersPadding: PaddingValues = PaddingValues()
) {
    item {
        Row(modifier = Modifier.padding(headersPadding)) {
            Text(
                text = headerText
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Zobacz wszystkie",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
    item {
        Spacer(modifier = Modifier.height(5.dp))
    }
    items(offers) {
        Spacer(modifier = Modifier.height(8.dp))
        val isExpanded = rememberSaveable(offers) {
            mutableStateOf(false)
        }
        GameOfferCard(
            gameOffer = it,
            isExpanded = { isExpanded.value },
            onExpandClick = {
                isExpanded.value = !isExpanded.value
            }
        )
    }
}

@Composable
fun SportHeader(
    sport: Sport,
    onBackClick: () -> Unit = {},
) {
    Card(modifier  = Modifier
        .fillMaxWidth()
        .height(150.dp), shape = RectangleShape) {
        Box {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                model = sport.sportBackgroundUrl,
                contentDescription = ""
            )
            IconButton(modifier = Modifier.align(Alignment.TopStart), onClick = { onBackClick() }) {
                Icon(
                    painter = painterResource(id = com.pawlowski.sportnite.R.drawable.back_icon),
                    contentDescription = "",
                    tint = Color.White
                )
            }
        }
    }
}

@OrbitInternal
@Preview(showBackground = true)
@Composable
fun SportScreenPreview() {
    SportScreen(viewModel = object : OrbitMviPreviewViewModel<SportScreenUiState, SportScreenSideEffect>(), ISportScreenViewModel {
        override fun stateForPreview(): SportScreenUiState {
            return SportScreenUiState(
                sport = getSportForPreview(),

            )
        }

    })
}