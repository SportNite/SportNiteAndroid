package com.pawlowski.sportnite.presentation.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pawlowski.sportnite.R
import com.pawlowski.sportnite.presentation.models.AdvanceLevel
import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.sportnite.presentation.ui.reusable_components.PlayerCard
import com.pawlowski.sportnite.presentation.ui.reusable_components.SportInputField
import com.pawlowski.sportnite.presentation.view_models_related.find_players_screen.FindPlayersScreenViewModel
import com.pawlowski.sportnite.presentation.view_models_related.find_players_screen.IFindPlayersScreenViewModel
import com.pawlowski.sportnite.utils.UiData

@Composable
fun FindPlayersScreen(
    modifier: Modifier = Modifier,
    viewModel: IFindPlayersScreenViewModel = hiltViewModel<FindPlayersScreenViewModel>(),
    onNavigateToHomeScreen: () -> Unit = {},
) {
    BackHandler {
        onNavigateToHomeScreen()
    }

    val uiState = viewModel.container.stateFlow.collectAsState()
    val searchInputState = remember {
        derivedStateOf {
            uiState.value.searchInput
        }
    }
    val sportInputState = remember {
        derivedStateOf {
            uiState.value.sportFilterInput
        }
    }
    val levelInputState = remember {
        derivedStateOf {
            uiState.value.advanceLevelFilterInput
        }
    }
    val playersDataState = remember {
        derivedStateOf {
            uiState.value.players
        }
    }

    val playersValueState = remember {
        derivedStateOf {
            val value = playersDataState.value
            if(value is UiData.Success)
                value.data
            else
                listOf()
        }
    }
    Surface(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            FiltersCard(
                searchInput = { searchInputState.value },
                sportInput = { sportInputState.value },
                advanceLevelInput = { levelInputState.value },
                onSearchInputChange = { viewModel.changeSearchInput(it) },
                onSportInputChange = { viewModel.changeSportFilterInput(it) },
                onAdvanceLevelInputChange = { viewModel.changeLevelFilterInput(it) },
                onApplyFiltersClick = { viewModel.applyFilters() },
                onClearFiltersClick = { viewModel.clearFilters() }
            )

            Spacer(modifier = Modifier.height(10.dp))

            val players = playersValueState.value
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(players.size) {
                    PlayerCard(modifier = Modifier.padding(5.dp), player = players[it])
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersCard(
    modifier: Modifier = Modifier,
    searchInput: () -> String,
    sportInput: () -> Sport?,
    advanceLevelInput: () -> AdvanceLevel?,
    onSearchInputChange: (String) -> Unit,
    onSportInputChange: (Sport) -> Unit,
    onAdvanceLevelInputChange: (AdvanceLevel) -> Unit,
    onClearFiltersClick: () -> Unit,
    onApplyFiltersClick: () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(5.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.9f),
                value = searchInput(),
                label = {
                        Text(text = "Wpisz aby wyszukać")
                },
                onValueChange = onSearchInputChange,
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.search_icon), contentDescription = "")
                }
            )
            Spacer(modifier = Modifier.height(10.dp))

            SportInputField(modifier = Modifier.fillMaxWidth(0.9f), chosenSport = sportInput(), onClick = {

            })

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.Center) {
                Button(onClick = { onClearFiltersClick() }) {
                    Text(text = "Wyczyść filtry")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = { onApplyFiltersClick() }) {
                    Text(text = "Zastosuj")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun FindPlayersScreenPreview() {
//    FindPlayersScreen()
//}