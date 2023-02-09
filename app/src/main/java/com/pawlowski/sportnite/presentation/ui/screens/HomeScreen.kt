package com.pawlowski.sportnite.presentation.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pawlowski.sportnite.R
import com.pawlowski.models.Sport
import com.pawlowski.models.User
import com.pawlowski.models.mappers.getMeetingsListForPreview
import com.pawlowski.models.mappers.getUserForPreview
import com.pawlowski.sportnite.presentation.ui.reusable_components.DisappearingSwipeRefresh
import com.pawlowski.commonui.IncomingMeetingsRow
import com.pawlowski.commonui.SportCard
import com.pawlowski.commonui.utils.OrbitMviPreviewViewModel
import com.pawlowski.sportnite.presentation.view_models_related.home_screen.HomeScreenSideEffect
import com.pawlowski.sportnite.presentation.view_models_related.home_screen.HomeScreenUiState
import com.pawlowski.sportnite.presentation.view_models_related.home_screen.HomeScreenViewModel
import com.pawlowski.sportnite.presentation.view_models_related.home_screen.IHomeScreenViewModel
import com.pawlowski.utils.UiData
import com.pawlowski.utils.dataOrNull
import org.orbitmvi.orbit.annotation.OrbitInternal

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: IHomeScreenViewModel = hiltViewModel<HomeScreenViewModel>(),
    onNavigateToSportScreen: (Sport) -> Unit = {},
    onNavigateToSettingsScreen: () -> Unit = {},
    onNavigateToMeetingDetails: (String) -> Unit = {},
    onNavigateToFullScreenList: (String) -> Unit = {},
    onNavigateToNotificationsScreen: () -> Unit = {}
) {
    val isExitAppDialogVisible = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    BackHandler {
        isExitAppDialogVisible.value = true
    }
    if(isExitAppDialogVisible.value) {
        AlertDialog(
            onDismissRequest = { isExitAppDialogVisible.value = false },
            text = { Text(text = "Napewno chcesz opuścić aplikację?") },
            confirmButton = {
                TextButton(onClick = {
                    (context as? Activity)?.finish()
                }) {
                    Text(text = "Tak")
                }
            },
            title = {
                Text(text = "Zamknięcie aplikacji")
            },
            dismissButton = {
                TextButton(onClick = { isExitAppDialogVisible.value = false }) {
                    Text(text = "Anuluj")
                }
            }
        )
    }

    val uiState = viewModel.container.stateFlow.collectAsState()
    val userState = remember {
        derivedStateOf {
            uiState.value.user
        }
    }


    val meetingsDataState = remember {
        derivedStateOf {
            uiState.value.upcomingMeetings
        }
    }


    val sportsValueState = remember {
        derivedStateOf {
            uiState.value.userSports.dataOrNull() ?: listOf()
        }
    }
    Surface(modifier.fillMaxSize()) {
        DisappearingSwipeRefresh(onRefresh = {
            viewModel.refreshData()
        }) {
            Column(modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())) {
                Spacer(modifier = Modifier.height(10.dp))
                ProfileSegment(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    user = userState.value,
                    onSettingsButtonClick = onNavigateToSettingsScreen,
                    onNotificationsButtonClick = onNavigateToNotificationsScreen
                )
                Spacer(modifier = Modifier.height(20.dp))
                IncomingMeetingsRow(
                    headersPadding = PaddingValues(horizontal = 10.dp),
                    meetings = meetingsDataState.value.dataOrNull(),
                    onMeetingCardClick = {
                        onNavigateToMeetingDetails(it.meetingUid)
                    },
                    onSeeMoreClick = {
                        onNavigateToFullScreenList("Meetings")
                    },
                    isLoading = { meetingsDataState.value is UiData.Loading<*> },
                    displaySeeMore = false
                )
                Spacer(modifier = Modifier.height(20.dp))

                SportsRow(
                    headersPadding = PaddingValues(horizontal = 10.dp),
                    sports = sportsValueState.value,
                    onSportClick = {
                        onNavigateToSportScreen(it)
                    },
                    onSeeMoreClick = {
                        onNavigateToFullScreenList("Sports")
                    }
                )
            }
        }

    }
}

@Composable
fun SportsRow(
    modifier: Modifier = Modifier,
    headersPadding: PaddingValues = PaddingValues(),
    sports: List<Sport>,
    onSportClick: (Sport) -> Unit = {},
    onSeeMoreClick: () -> Unit = {}
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.padding(headersPadding), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Twoje sporty")
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = onSeeMoreClick) {
                Text(text = "Zobacz wszystkie", color = MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        LazyRow {
            item { Spacer(modifier = Modifier.width(5.dp)) }
            items(sports) { sport ->
                SportCard(
                    sport = sport,
                    onSportClick = {
                        onSportClick(it)
                    }
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

@Composable
fun ProfileSegment(
    modifier: Modifier = Modifier,
    user: User?,
    onSettingsButtonClick: () -> Unit = {},
    onNotificationsButtonClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        ProfileImageCard(
            modifier = Modifier.size(50.dp),
            profileUrl = user?.userPhotoUrl ?: ""
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.height(50.dp), verticalArrangement = Arrangement.Center) {
            Text(text = "Witaj!")
            Spacer(modifier = Modifier.height(3.dp))
            Text(text = user?.userName ?: "")
        }
        Spacer(modifier = Modifier.weight(1f))
        FilledIconButton(onClick = {
            onNotificationsButtonClick()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.notifications_icon),
                contentDescription = ""
            )
        }
        FilledIconButton(onClick = {
            onSettingsButtonClick()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.settings_icon),
                contentDescription = ""
            )
        }
    }
}


@Composable
fun ProfileImageCard(modifier: Modifier = Modifier, profileUrl: String) {
    Card(modifier = modifier, shape = CircleShape) {
        if (profileUrl.isNotEmpty()) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = profileUrl,
                contentDescription = "profile image",
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

@OptIn(OrbitInternal::class)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(viewModel = object :
        OrbitMviPreviewViewModel<HomeScreenUiState, HomeScreenSideEffect>(), IHomeScreenViewModel {
        override fun stateForPreview(): HomeScreenUiState {
            return HomeScreenUiState(
                user = getUserForPreview(),
                upcomingMeetings = UiData.Success(
                    isFresh = true,
                    data = getMeetingsListForPreview()
                )
            )
        }

        override fun refreshData() {
            TODO("Not yet implemented")
        }

    })
}