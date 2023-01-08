package com.pawlowski.sportnite.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.pawlowski.sportnite.data.mappers.availableSports
import com.pawlowski.sportnite.presentation.models.GameOffer
import com.pawlowski.sportnite.presentation.models.Meeting
import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.sportnite.presentation.ui.reusable_components.*
import com.pawlowski.sportnite.presentation.view_models_related.full_screen_list_screen.FullScreenDataType
import com.pawlowski.sportnite.presentation.view_models_related.full_screen_list_screen.FullScreenListViewModel
import com.pawlowski.sportnite.presentation.view_models_related.full_screen_list_screen.IFullScreenListViewModel

@Composable
fun FullScreenListScreen(
    modifier: Modifier = Modifier,
    viewModel: IFullScreenListViewModel = hiltViewModel<FullScreenListViewModel>(),
    onNavigateBack: () -> Unit = {},
    onNavigateToSportScreen: (Sport) -> Unit = {}
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
            Spacer(modifier = Modifier.height(5.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = dataTypeState.value.name.asString())
                IconButton(modifier = Modifier.align(Alignment.CenterStart), onClick = onNavigateBack) {
                    Icon(painter = painterResource(id = com.pawlowski.sportnite.R.drawable.back_icon), contentDescription = "")
                }
            }

            LazyVerticalGrid(columns = GridCells.Fixed(dataTypeState.value.columnsInRow)) {


                item(span = { GridItemSpan(dataTypeState.value.columnsInRow) }) {
                    Spacer(modifier = Modifier.height(10.dp))
                }
                displayItemsBasedOnDataType(
                    dataType = dataTypeState.value,
                    lazyPagingOffers = { lazyPagingOffers },
                    lazyPagingMeetings = { lazyPagingMeetings },
                    sports = { sports },
                    onSportClick = {
                        onNavigateToSportScreen(it)
                    }
                )

            }
        }
    }
}



private fun LazyGridScope.displayItemsBasedOnDataType(
    dataType: FullScreenDataType,
    lazyPagingOffers: () -> LazyPagingItems<GameOffer>,
    lazyPagingMeetings: () -> LazyPagingItems<Meeting>,
    sports: () -> List<Sport>,
    onSportClick: (Sport) -> Unit = {},
) {
    val pagingItems = when(dataType) {
        is FullScreenDataType.OffersData -> {
            lazyPagingOffers()
        }
        is FullScreenDataType.MeetingsData -> {
            lazyPagingMeetings()
        }
        is FullScreenDataType.OffersToAccept -> {
            null
        }
        is FullScreenDataType.UserSportsData -> {
            null
        }
    }

    item(span = { GridItemSpan(2) }) {
        if(pagingItems?.areNoResults() == true) {
            NoItemsFoundCard()
        }
    }
    item(span = { GridItemSpan(2) }) {
        when(pagingItems?.loadState?.refresh)
        {
            is LoadState.Loading -> {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .size(30.dp),
                    )
                }
            }
            is LoadState.Error -> {
                Button(onClick = { pagingItems.retry() }) {
                    Text(text = "Try again")
                }
            }
            else -> {}
        }
    }
    when(dataType) {
        is FullScreenDataType.OffersData -> {
            items(lazyPagingOffers().itemCount) {
                val item = lazyPagingOffers()[it]
                val isExpanded = rememberSaveable {
                    mutableStateOf(false)
                }
                if (item != null) {
                    GameOfferCard(
                        gameOffer = item,
                        textButtonText = { Text(text = "text")}, //TODO: handle clicks and display correct text
                        onExpandClick = {
                            isExpanded.value = !isExpanded.value
                        },
                        isExpanded = { isExpanded.value }
                    )
                }
            }
        }
        is FullScreenDataType.MeetingsData -> {
            items(lazyPagingMeetings().itemCount) {
                val item = lazyPagingMeetings()[it]
                if (item != null) {
                    MeetingCard(meeting = item)
                }
            }
        }
        is FullScreenDataType.UserSportsData -> {
            items(sports()) { sportItem ->
                SportCard(modifier = Modifier.padding(horizontal = 5.dp), sport = sportItem) {
                    onSportClick(it)
                }
            }
        }
        is FullScreenDataType.OffersToAccept -> {

        }
    }

    item(span = { GridItemSpan(2) })
    {
        when(pagingItems?.loadState?.append)
        {
            is LoadState.Loading -> {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .size(30.dp),
                    )
                }
            }
            is LoadState.Error -> {
                Button(onClick = { pagingItems.retry() }) {
                    Text(text = "Try again")
                }
            }
            else -> {}
        }

    }
}
