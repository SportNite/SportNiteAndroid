package com.pawlowski.sportnite.presentation.ui.screens

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pawlowski.sportnite.R
import com.pawlowski.sportnite.presentation.view_models_related.settings_screen.ISettingsScreenViewModel
import com.pawlowski.sportnite.presentation.view_models_related.settings_screen.SettingsScreenSideEffect
import com.pawlowski.sportnite.presentation.view_models_related.settings_screen.SettingsScreenViewModel


@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: ISettingsScreenViewModel = hiltViewModel<SettingsScreenViewModel>(),
    onNavigateToLoginScreen: () -> Unit = {}
) {
    LaunchedEffect(true) {
        viewModel.container.sideEffectFlow.collect { event ->
            when (event) {
                is SettingsScreenSideEffect.NavigateToLoginScreen -> {
                    onNavigateToLoginScreen()
                }
            }
        }
    }

    val uiState = viewModel.container.stateFlow.collectAsState()
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Konto")
        Spacer(modifier = Modifier.height(10.dp))
        AccountCard(
            modifier = Modifier.padding(horizontal = 10.dp),
            displayName = uiState.value.player?.userName?:"",
            mail = uiState.value.player?.userPhoneNumber?:"",
            profilePhoto = uiState.value.player?.userPhotoUrl?.let {
               if(it.isNotEmpty())
                   Uri.parse(it)
                else
                    null
            },
        )
        Spacer(modifier = Modifier.height(20.dp))
        OptionsCard(
            modifier = Modifier.padding(horizontal = 10.dp),
            onLogOutClick = {
                viewModel.signOut()
            },
            onMyAccountClick = {

            },
        )


    }
}

@Composable
fun OptionsCard(
    modifier: Modifier = Modifier,
    onMyAccountClick: () -> Unit,
    onLogOutClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Column {

            Spacer(modifier = Modifier.height(5.dp))

            OptionRow(
                padding = PaddingValues(horizontal = 15.dp, vertical = 15.dp),
                iconId = R.drawable.account_icon,
                tittle = "Moje konto",
                label = "Dokonaj zmian na swoim koncie"
            ) {
                onMyAccountClick()
            }

            OptionRow(
                padding = PaddingValues(horizontal = 15.dp, vertical = 15.dp),
                iconId = R.drawable.sign_out_icon,
                tittle = "Wyloguj się",
                label = ""
            ) {
                onLogOutClick()
            }

            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

@Composable
fun OptionRow(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(),
    iconId: Int,
    tittle: String,
    label: String,
    onClick: () -> Unit
) {
    Row(modifier = modifier
        .clickable { onClick() }
        .padding(padding)
        .height(40.dp)
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically

    ) {

        Card(
            shape = CircleShape,
            modifier = Modifier.size(40.dp),
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                painter = painterResource(id = iconId),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly) {
            Text(
                text = tittle,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp
            )
            if (label.isNotEmpty()) {
                Text(
                    text = label,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        Icon(painter = painterResource(id = R.drawable.arrow_right_icon), contentDescription = "")

    }
}

@Composable
fun AccountCard(
    modifier: Modifier = Modifier,
    displayName: String,
    mail: String,
    profilePhoto: Uri?,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(89.dp),
    )
    {
        Row(modifier = Modifier.fillMaxHeight(),verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(15.dp))
            Card(
                shape = CircleShape,
                modifier = Modifier.size(53.dp),
            ) {
                profilePhoto?.let {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = it,
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds
                    )
                } ?: kotlin.run {
                    Icon(
                        painter = painterResource(id = R.drawable.account_circle_icon),
                        contentDescription = "",
                    )
                }

            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = displayName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = mail,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp
                )
            }
        }
    }
}
