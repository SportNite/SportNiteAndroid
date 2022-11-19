package com.pawlowski.sportnite.presentation.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import org.orbitmvi.orbit.annotation.OrbitInternal

@Composable
fun SportScreen(viewModel: ISportScreenViewModel = hiltViewModel<SportScreenViewModel>()) {
    val uiState = viewModel.container.stateFlow.collectAsState()
    val sportState = remember {
        derivedStateOf {
            uiState.value.sport
        }
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            SportHeader(sport = sportState.value)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = sportState.value.sportName.asString(), fontSize = 20.sp)
            Spacer(modifier = Modifier.height(5.dp))
            GameOffersColumn(
                offers = getGameOfferListForPreview().take(3),
                headerText = "Do akceptacji",
                headersPadding = PaddingValues(horizontal = 10.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))

            IncomingMeetingsRow(
                meetings = getMeetingsListForPreview(),
                headersPadding = PaddingValues(horizontal = 10.dp)
            )

            Spacer(modifier = Modifier.height(5.dp))

            GameOffersColumn(
                offers = getGameOfferListForPreview().take(4),
                headerText = "Oferty na grę",
                headersPadding = PaddingValues(horizontal = 10.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

@Composable
fun GameOffersColumn(
    offers: List<GameOffer>,
    headerText: String = "Oferty na grę",
    headersPadding: PaddingValues = PaddingValues()
) {
    LazyColumn {
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
            val isExpanded = remember {
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