package com.pawlowski.sportnite.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pawlowski.sportnite.presentation.view_models_related.waiting_for_user_info_screen.IWaitingForUserInfoViewModel
import com.pawlowski.sportnite.presentation.view_models_related.waiting_for_user_info_screen.WaitingForUserInfoSideEffect
import com.pawlowski.sportnite.presentation.view_models_related.waiting_for_user_info_screen.WaitingForUserInfoViewModel

@Composable
fun WaitingForUserInfoScreen(
    viewModel: IWaitingForUserInfoViewModel = hiltViewModel<WaitingForUserInfoViewModel>(),
    onNavigateToHomeScreen: () -> Unit = {},
    onNavigateToAccountDetailsScreen: () -> Unit = {},
    onNavigateToChooseSports: () -> Unit = {}
    ) {
    LaunchedEffect(Unit) {
        viewModel.checkUserInfo()
        viewModel.container.sideEffectFlow.collect { event ->
            when(event) {
                is WaitingForUserInfoSideEffect.NavigateToAccountDetailsScreen -> {
                    onNavigateToAccountDetailsScreen()
                }
                is WaitingForUserInfoSideEffect.NavigateToHomeScreen -> {
                    onNavigateToHomeScreen()
                }
                is WaitingForUserInfoSideEffect.NavigateToChooseSportsScreen -> {
                    onNavigateToChooseSports()
                }
            }
        }
    }
    val uiState = viewModel.container.stateFlow.collectAsState()
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column {
                Text(text = uiState.value.message)
                Spacer(modifier = Modifier.height(15.dp))
                if(uiState.value.isLoading)
                    CircularProgressIndicator()
                else
                {
                    Button(onClick = { viewModel.checkUserInfo() }, enabled = !uiState.value.isLoading) {
                        Text(text = "Spróbuj ponownie")
                    }
                }
            }
        }
    }
}