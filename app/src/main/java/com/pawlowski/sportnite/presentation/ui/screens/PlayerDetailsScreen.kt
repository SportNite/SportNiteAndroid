package com.pawlowski.sportnite.presentation.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pawlowski.sportnite.presentation.models.AdvanceLevel
import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.sportnite.presentation.view_models_related.player_details.IPlayerDetailsViewModel
import com.pawlowski.sportnite.presentation.view_models_related.player_details.PlayerDetailsViewModel
import com.pawlowski.sportnite.utils.dataOrNull


@Composable
fun PlayerDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: IPlayerDetailsViewModel = hiltViewModel<PlayerDetailsViewModel>(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState = viewModel.container.stateFlow.collectAsState()
    val playerDetailsState = remember {
        derivedStateOf {
            uiState.value.playerDetails.dataOrNull()
        }
    }

    val phoneState = remember {
        derivedStateOf {
            playerDetailsState.value?.contact?.getOrNull(0)?.ifEmpty { null }
        }
    }

    val timeAvailabilityState = remember {
        derivedStateOf {
            playerDetailsState.value?.timeAvailability?.ifEmpty { null }
        }
    }

    val ageState = remember {
        derivedStateOf {
            playerDetailsState.value?.age
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
                    AsyncImage(model = playerDetailsState.value?.playerPhotoUrl, contentDescription = "", contentScale = ContentScale.FillBounds)
                }
            }

            Text(text = playerDetailsState.value?.playerName?:"")
            ageState.value?.let {
                Text(text = "$it lat")

            }
            //Text(text = playerDetailsState.value?.city) //TODO: Add city label
            Spacer(modifier = Modifier.height(5.dp))
            Divider(modifier = Modifier.fillMaxWidth())

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                timeAvailabilityState.value?.let {
                    Text(text = "Dostępność czasowa", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = playerDetailsState.value?.timeAvailability?:"")
                    Spacer(modifier = Modifier.height(5.dp))
                }
                AdvanceLevelsRow(levels = playerDetailsState.value?.advanceLevels ?: mapOf())
                Spacer(modifier = Modifier.height(5.dp))

                ContactRow(contacts = playerDetailsState.value?.contact ?: listOf())
                Spacer(modifier = Modifier.height(5.dp))

                phoneState.value?.let {
                    Text(text = "Skontaktuj się", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(3.dp))
                }



            }
            val context = LocalContext.current


            phoneState.value?.let { phoneValue ->
                Button(onClick = {
                    val smsUri: Uri = Uri.parse("smsto:$phoneValue") //Replace the phone number
                    val sms = Intent(Intent.ACTION_VIEW, smsUri)
                    context.startActivity(sms)
                }) {
                    Text(text = "Wyślij SMS")
                }

                Spacer(modifier = Modifier.height(5.dp))
                val clipboardManager = LocalClipboardManager.current
                Button(onClick = {
                    clipboardManager.setText(AnnotatedString(text = phoneValue))
                    Toast.makeText(context, "Numer telefonu został skopiowany do schowka!", Toast.LENGTH_LONG).show()
                }) {
                    Text(text = "Skopiuj numer telefonu")
                }
            }



        }
    }
}

@Composable
fun AdvanceLevelCard(sport: Sport, advanceLevel: AdvanceLevel) {
    Card {
        Column(modifier = Modifier.padding(3.dp)) {
            Text(text = sport.sportName.asString())
            Row {
                Text(text = advanceLevel.asString)
                Spacer(modifier = Modifier.width(5.dp))
                Icon(modifier = Modifier.size(15.dp), painter = painterResource(id = sport.sportIconId), contentDescription = "")
            }
        }
    }
}

@Composable
fun AdvanceLevelsRow(levels: Map<Sport, AdvanceLevel>) {
    if(levels.isNotEmpty()) {
        Column {
            Text(text = "Sporty", fontWeight = FontWeight.Bold)
            val levelsList = remember(levels) {
                levels.toList()
            }
            Spacer(modifier = Modifier.height(5.dp))
            LazyRow {
                item {
                    Spacer(modifier = Modifier.width(10.dp))
                }
                items(levelsList) {
                    AdvanceLevelCard(it.first, it.second)
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }
    }
}

@Composable
fun ContactRow(contacts: List<String>) {
    if(contacts.isNotEmpty() && !contacts.all { it.isEmpty() }) {
        Column {
            Text(text = "Kontakt", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(3.dp))
            LazyRow {
                item {
                    Spacer(modifier = Modifier.width(10.dp))

                }
                items(contacts) {
                    if(it.isNotEmpty())
                    {
                        Card {
                            Text(modifier = Modifier.padding(3.dp), text = it)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                }
            }
        }
    }
}
