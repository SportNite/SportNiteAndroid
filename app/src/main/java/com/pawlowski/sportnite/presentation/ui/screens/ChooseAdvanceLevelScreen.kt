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
import com.pawlowski.sportnite.data.mappers.getAvailableLevelsForSport
import com.pawlowski.sportnite.presentation.models.AdvanceLevel
import com.pawlowski.sportnite.presentation.ui.utils.OrbitMviPreviewViewModel
import com.pawlowski.sportnite.presentation.ui.utils.getSportForPreview
import com.pawlowski.sportnite.presentation.view_models_related.choose_advance_level_screen.ChooseAdvanceLevelScreenSideEffect
import com.pawlowski.sportnite.presentation.view_models_related.choose_advance_level_screen.ChooseAdvanceLevelScreenUiState
import com.pawlowski.sportnite.presentation.view_models_related.choose_advance_level_screen.ChooseAdvanceLevelScreenViewModel
import com.pawlowski.sportnite.presentation.view_models_related.choose_advance_level_screen.IChooseAdvanceLevelScreenViewModel
import org.orbitmvi.orbit.annotation.OrbitInternal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseAdvanceLevelScreen(
    modifier: Modifier = Modifier,
    viewModel: IChooseAdvanceLevelScreenViewModel = hiltViewModel<ChooseAdvanceLevelScreenViewModel>(),
    onNavigateToChooseSportsScreen: () -> Unit = {},
    onNavigateToHomeScreen: () -> Unit = {}
) {
    BackHandler {
        viewModel.navigateBack()
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { event ->
            when(event) {
                is ChooseAdvanceLevelScreenSideEffect.ShowToastMessage-> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_LONG).show()
                }
                is ChooseAdvanceLevelScreenSideEffect.NavigateToChooseSportsScreen -> {
                    onNavigateToChooseSportsScreen()
                }
                is ChooseAdvanceLevelScreenSideEffect.NavigateToHomeScreen -> {
                    onNavigateToHomeScreen()
                }
            }
        }
    }

    Surface(Modifier.fillMaxSize()) {
        Column {
            val uiState = viewModel.container.stateFlow.collectAsState()
            val chosenLevelState = remember {
                derivedStateOf {
                    uiState.value.chosenLevel
                }
            }
            Surface(modifier = modifier.fillMaxSize()) {
                val availableLevels = uiState.value.availableLevels?: listOf()
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Zaawansowanie", fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "Określ swój poziom zaawansowania (1-10) dla sportu:")
                    Text(text = uiState.value.currentSport.sportName.asString(), fontWeight = FontWeight.Medium)

                    Spacer(modifier = Modifier.height(15.dp))

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(availableLevels) {
                            ListItem(
                                headlineText = { Text(it.asString) },
                                leadingContent = {
                                    RadioButton(selected = chosenLevelState.value == it, onClick = {
                                        viewModel.selectLevel(it)
                                    })
                                },
                                shadowElevation = 5.dp
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                        }

                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        onClick = { viewModel.continueClick() },
                        enabled = !uiState.value.isLoading
                    )
                    {
                        Text(text = "Kontynuuj")
                    }
                    Spacer(modifier = Modifier.height(15.dp))

                }
            }
        }
    }
}

@OptIn(OrbitInternal::class)
@Preview(showBackground = true)
@Composable
fun ChooseAdvanceLevelScreenPreview() {
    ChooseAdvanceLevelScreen(
        viewModel = object :
            OrbitMviPreviewViewModel<ChooseAdvanceLevelScreenUiState, ChooseAdvanceLevelScreenSideEffect>(),
            IChooseAdvanceLevelScreenViewModel {
            override fun stateForPreview(): ChooseAdvanceLevelScreenUiState {
                return ChooseAdvanceLevelScreenUiState(
                    currentSport = getSportForPreview(),
                    availableLevels = getAvailableLevelsForSport(getSportForPreview()),
                    chosenLevel = getAvailableLevelsForSport(getSportForPreview())[2]
                )
            }
            override fun selectLevel(level: AdvanceLevel) {}
            override fun continueClick() {}
            override fun navigateBack() {}
        }
    )
}