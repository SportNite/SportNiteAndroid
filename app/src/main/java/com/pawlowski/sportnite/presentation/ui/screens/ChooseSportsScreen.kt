package com.pawlowski.sportnite.presentation.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pawlowski.models.mappers.availableSports
import com.pawlowski.models.Sport
import com.pawlowski.sportnite.presentation.ui.utils.OrbitMviPreviewViewModel
import com.pawlowski.sportnite.presentation.view_models_related.choose_sports_screen.ChooseSportsScreenSideEffect
import com.pawlowski.sportnite.presentation.view_models_related.choose_sports_screen.ChooseSportsScreenUiState
import com.pawlowski.sportnite.presentation.view_models_related.choose_sports_screen.ChooseSportsScreenViewModel
import com.pawlowski.sportnite.presentation.view_models_related.choose_sports_screen.IChooseSportsScreenViewModel
import org.orbitmvi.orbit.annotation.OrbitInternal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseSportsScreen(
    modifier: Modifier = Modifier,
    viewModel: IChooseSportsScreenViewModel = hiltViewModel<ChooseSportsScreenViewModel>(),
    onNavigateToChoseAdvanceLevelScreen: () -> Unit = {}
) {
    BackHandler {

    }
    val uiState = viewModel.container.stateFlow.collectAsState()
    val isLoadingState = remember {
        derivedStateOf {
            uiState.value.isLoading
        }
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { event ->
            when(event) {
                is ChooseSportsScreenSideEffect.NavigateToChoseAdvanceLevelScreen -> {
                    onNavigateToChoseAdvanceLevelScreen()
                }
                is ChooseSportsScreenSideEffect.ShowToastMessage -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    Surface(modifier = modifier.fillMaxSize()) {
        val sportsMapAsList = uiState.value.sports.toList()
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Zainteresowania sportowe", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Wybierz sporty które cię interesują")
            Spacer(modifier = Modifier.height(15.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(sportsMapAsList) {
                    ListItem(
                        headlineText = { Text(it.first.sportName.asString()) },
                        leadingContent = {
                            Checkbox(
                                checked = it.second,
                                onCheckedChange = { _ ->
                                    viewModel.changeSelectionOfSport(it.first)
                                }, enabled = !isLoadingState.value
                            )
                        },
                        shadowElevation = 5.dp
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                }

            }

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                modifier = Modifier.fillMaxWidth(0.8f),
                enabled = !isLoadingState.value,
                onClick = { viewModel.continueClick() }) {
                Text(text = "Kontynuuj")
            }
            Spacer(modifier = Modifier.height(15.dp))

        }
    }
}


@OptIn(OrbitInternal::class)
@Preview(showBackground = true)
@Composable
private fun ChooseSportsScreenPreview() {
    ChooseSportsScreen(
        viewModel = object :
            OrbitMviPreviewViewModel<ChooseSportsScreenUiState, ChooseSportsScreenSideEffect>(),
            IChooseSportsScreenViewModel {
            override fun stateForPreview(): ChooseSportsScreenUiState {
                return ChooseSportsScreenUiState(sports = availableSports.values.associateWith { false })
            }

            override fun changeSelectionOfSport(sport: Sport) {}
            override fun continueClick() {}
            override fun navigateBack() {}
        }
    )
}