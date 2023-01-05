package com.pawlowski.sportnite.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.pawlowski.sportnite.data.mappers.availableSports
import com.pawlowski.sportnite.presentation.ui.reusable_components.GameOfferCard
import com.pawlowski.sportnite.presentation.ui.reusable_components.MeetingCard
import com.pawlowski.sportnite.presentation.ui.reusable_components.SportCard
import com.pawlowski.sportnite.presentation.view_models_related.full_screen_list_screen.FullScreenDataType
import com.pawlowski.sportnite.presentation.view_models_related.full_screen_list_screen.FullScreenListViewModel
import com.pawlowski.sportnite.presentation.view_models_related.full_screen_list_screen.IFullScreenListViewModel

@Composable
fun FullScreenListScreen(
    modifier: Modifier = Modifier,
    viewModel: IFullScreenListViewModel = hiltViewModel<FullScreenListViewModel>()
) {
    val uiState = viewModel.container.stateFlow.collectAsState()

    Surface(modifier = modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            val lazyPagingOffers = viewModel.offersFlow.collectAsLazyPagingItems()
            val lazyPagingMeetings = viewModel.meetingsFlow.collectAsLazyPagingItems()
            val dataTypeState = viewModel.dataTypeFlow.collectAsState()
            val sports = remember {
                availableSports.values.toList()
            }
            LazyVerticalGrid(columns = GridCells.Fixed(dataTypeState.value.columnsInRow)) {
                item(span = { GridItemSpan(dataTypeState.value.columnsInRow) }) {
                    Spacer(modifier = Modifier.height(10.dp))
                }
                when(dataTypeState.value) {
                    is FullScreenDataType.OffersData -> {
                        items(lazyPagingOffers.itemCount) {
                            val item = lazyPagingOffers[it]
                            if (item != null) {
                                GameOfferCard(gameOffer = item, textButtonText = { Text(text = "text")})
                            }
                        }
                    }
                    is FullScreenDataType.MeetingsData -> {
                        items(lazyPagingMeetings.itemCount) {
                            val item = lazyPagingMeetings[it]
                            if (item != null) {
                                MeetingCard(meeting = item)
                            }
                        }
                    }
                    is FullScreenDataType.UserSportsData -> {
                        items(sports) {
                            SportCard(modifier = Modifier.padding(horizontal = 5.dp), sport = it)
                        }
                    }
                }

            }
        }
    }
}