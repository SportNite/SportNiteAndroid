package com.pawlowski.sportnite.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.pawlowski.sportnite.data.mappers.availableSports
import com.pawlowski.models.GameOffer
import com.pawlowski.models.Sport
import com.pawlowski.sportnite.presentation.ui.reusable_components.*
import com.pawlowski.sportnite.presentation.view_models_related.full_screen_list_screen.FullScreenDataType
import com.pawlowski.sportnite.presentation.view_models_related.full_screen_list_screen.FullScreenListSideEffect
import com.pawlowski.sportnite.presentation.view_models_related.full_screen_list_screen.FullScreenListViewModel
import com.pawlowski.sportnite.presentation.view_models_related.full_screen_list_screen.IFullScreenListViewModel

@Composable
fun FullScreenListScreen(
    modifier: Modifier = Modifier,
    viewModel: IFullScreenListViewModel = hiltViewModel<FullScreenListViewModel>(),
    onNavigateBack: () -> Unit = {},
    onNavigateToSportScreen: (Sport) -> Unit = {}
) {

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { event ->
            when(event) {
                is FullScreenListSideEffect.ShowToastMessage -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            val lazyPagingOffers = viewModel.offersFlow.collectAsLazyPagingItems()
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
                    sports = { sports },
                    onSportClick = {
                        onNavigateToSportScreen(it)
                    },
                    onRemoveOfferToAccept = { viewModel.deleteMyOfferToAccept(it) }
                ) { viewModel.sendOfferToAccept(it) }

            }
        }
    }
}



private fun LazyGridScope.displayItemsBasedOnDataType(
    dataType: FullScreenDataType,
    lazyPagingOffers: () -> LazyPagingItems<GameOffer>,
    sports: () -> List<Sport>,
    onSportClick: (Sport) -> Unit = {},
    onRemoveOfferToAccept: (GameOffer) -> Unit = {},
    onSendOfferToAccept: (GameOffer) -> Unit = {}
) {

    if(dataType !is FullScreenDataType.UserSportsData) {
        displayPagingItemsWithIndicators(
            pagingItems = lazyPagingOffers(),
            itemContent = {
                val item = it
                val isExpanded = rememberSaveable {
                    mutableStateOf(false)
                }
                GameOfferCard(
                    gameOffer = item,
                    onTextButtonClick = {
                        if(item.myResponseIdIfExists != null) {
                            onRemoveOfferToAccept(item)
                        }
                        else
                        {
                            onSendOfferToAccept(item)
                        }
                    },
                    textButtonText = {
                        if(item.myResponseIdIfExists != null) {
                            Text(text = "Anuluj moją propozycję", color = Color.Red)
                        }
                        else
                        {
                            Text(text = "Akceptuj propozycję")
                        }
                    },
                    onExpandClick = {
                        isExpanded.value = !isExpanded.value
                    },
                    isExpanded = { isExpanded.value }
                )

            }
        )
    }
    else {
        items(sports()) { sportItem ->
            SportCard(modifier = Modifier.padding(horizontal = 5.dp), sport = sportItem) {
                onSportClick(it)
            }
        }
    }
}
