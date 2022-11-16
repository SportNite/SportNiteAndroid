package com.pawlowski.sportnite.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pawlowski.sportnite.R
import com.pawlowski.sportnite.presentation.models.Meeting
import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.sportnite.presentation.models.User
import com.pawlowski.sportnite.presentation.ui.reusable_components.MeetingCard
import com.pawlowski.sportnite.presentation.ui.reusable_components.SportCard
import com.pawlowski.sportnite.presentation.ui.utils.*
import com.pawlowski.sportnite.presentation.view_models_related.home_screen.HomeScreenSideEffect
import com.pawlowski.sportnite.presentation.view_models_related.home_screen.HomeScreenUiState
import com.pawlowski.sportnite.presentation.view_models_related.home_screen.HomeScreenViewModel
import com.pawlowski.sportnite.presentation.view_models_related.home_screen.IHomeScreenViewModel
import com.pawlowski.sportnite.utils.UiData
import org.orbitmvi.orbit.annotation.OrbitInternal

@Composable
fun HomeScreen(viewModel: IHomeScreenViewModel = hiltViewModel<HomeScreenViewModel>()) {
    val uiState = viewModel.container.stateFlow.collectAsState()
    val userState = remember {
        derivedStateOf {
            uiState.value.user
        }
    }
    val meetingsState = remember {
        derivedStateOf {
            val meetingsUiData = uiState.value.upcomingMeetings
            if(meetingsUiData is UiData.Success)
                meetingsUiData.data
            else
                null
        }
    }
    Surface(Modifier.fillMaxSize()) {
        Column {
            Spacer(modifier = Modifier.height(10.dp))
            ProfileSegment(
                modifier = Modifier.padding(horizontal = 10.dp),
                user = userState.value
            )
            Spacer(modifier = Modifier.height(20.dp))
            IncomingMeetingsRow(
                headersPadding = PaddingValues(horizontal = 10.dp),
                meetings = meetingsState.value
            )
            Spacer(modifier = Modifier.height(20.dp))

            SportsRow(
                headersPadding = PaddingValues(horizontal = 10.dp),
                sports = listOf()
            )
        }
    }
}

@Composable
fun SportsRow(
    modifier: Modifier = Modifier,
    headersPadding: PaddingValues = PaddingValues(),
    sports: List<Sport>
)
{
    Column(modifier = modifier) {
        Row(modifier = Modifier.padding(headersPadding)) {
            Text(text = "Twoje sporty")
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Zobacz wszystkie")
        }
        Spacer(modifier = Modifier.height(5.dp))
        LazyRow {
            item { Spacer(modifier = Modifier.width(5.dp)) }
            items(3) {
                SportCard(sport = getSportForPreview())
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

@Composable
fun ProfileSegment(modifier: Modifier = Modifier, user: User?) {
    Row(modifier = modifier
        .fillMaxWidth()) {
        ProfileImageCard(
            modifier= Modifier.size(50.dp),
            profileUrl = user?.userPhotoUrl?:""
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.height(50.dp), verticalArrangement = Arrangement.Center) {
            Text(text = "Witaj!")
            Spacer(modifier = Modifier.height(3.dp))
            Text(text = user?.userName?:"")
        }
        Spacer(modifier = Modifier.weight(1f))
        FilledIconButton(onClick = { /*TODO*/ }) {
            Icon(
                painter = painterResource(id = R.drawable.notifications_icon),
                contentDescription = ""
            )
        }
        FilledIconButton(onClick = { /*TODO*/ }) {
            Icon(
                painter = painterResource(id = R.drawable.settings_icon),
                contentDescription = ""
            )
        }
    }
}

@Composable
fun IncomingMeetingsRow(
    modifier: Modifier = Modifier,
    headersPadding: PaddingValues = PaddingValues(),
    meetings: List<Meeting>?
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.padding(headersPadding)) {
            Text(text = "Nadchodzące spotkania")
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Zobacz wszystkie")
        }
        Spacer(modifier = Modifier.height(5.dp))
        LazyRow {
            item { Spacer(modifier = Modifier.width(5.dp)) }
            meetings?.let {
                items(meetings) {
                    MeetingCard(meeting = it)
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }

        }
    }
}

@Composable
fun ProfileImageCard(modifier: Modifier = Modifier, profileUrl: String) {
    Card(modifier = modifier, shape = CircleShape) {
        if(profileUrl.isNotEmpty())
        {
            AsyncImage(modifier = Modifier.fillMaxSize(), model = profileUrl, contentDescription = "profile image")
        }
    }
}

@OptIn(OrbitInternal::class)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(viewModel = object : OrbitMviPreviewViewModel<HomeScreenUiState, HomeScreenSideEffect>(), IHomeScreenViewModel {
        override fun stateForPreview(): HomeScreenUiState {
            return HomeScreenUiState(
                user = getUserForPreview(),
                upcomingMeetings = UiData.Success(
                    isFresh = true,
                    data = getMeetingsListForPreview()
                )
            )
        }

    })
}