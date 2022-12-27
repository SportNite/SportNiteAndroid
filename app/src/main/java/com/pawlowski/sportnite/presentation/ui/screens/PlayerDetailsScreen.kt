package com.pawlowski.sportnite.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import coil.compose.AsyncImage
import com.pawlowski.sportnite.presentation.ui.reusable_components.VerticalDivider
import com.pawlowski.sportnite.presentation.view_models_related.player_details.IPlayerDetailsViewModel
import com.pawlowski.sportnite.presentation.view_models_related.player_details.PlayerDetailsViewModel
import com.pawlowski.sportnite.utils.UiData

@Composable
fun PlayerDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: IPlayerDetailsViewModel = hiltViewModel<PlayerDetailsViewModel>(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState = viewModel.container.stateFlow.collectAsState()
    val playerDetailsState = remember {
        derivedStateOf {
            val value = uiState.value.playerDetails
            if(value is UiData.Success) {
                value.data
            }
            else
                null
        }
    }
    Surface(modifier = modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.height(170.dp)) {
                Card(
                    modifier= Modifier
                        .fillMaxWidth()
                        .height(139.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RectangleShape
                ) {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(painter = painterResource(id = com.pawlowski.sportnite.R.drawable.back_icon), contentDescription = "")
                    }
                }
                Card(shape = CircleShape, modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(100.dp)) {
                    AsyncImage(model = playerDetailsState.value?.playerPhotoUrl, contentDescription = "")
                }
            }

            Text(text = playerDetailsState.value?.playerName?:"")
            Text(text = "${playerDetailsState.value?.age ?: ""} lat")
            //Text(text = playerDetailsState.value?.city) //TODO: Add city label
            Spacer(modifier = Modifier.height(5.dp))
            VerticalDivider(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Dostępność czasowa")
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = playerDetailsState.value?.timeAvailability?:"")

        }
    }
}