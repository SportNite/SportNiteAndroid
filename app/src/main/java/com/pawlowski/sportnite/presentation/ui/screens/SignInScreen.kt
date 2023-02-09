package com.pawlowski.sportnite.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pawlowski.sportnite.presentation.ui.reusable_components.LoginLottieAnimation
import com.pawlowski.commonui.utils.OrbitMviPreviewViewModel
import com.pawlowski.sportnite.presentation.view_models_related.sign_in_screen.ISignInScreenViewModel
import com.pawlowski.sportnite.presentation.view_models_related.sign_in_screen.SignInScreenSideEffect
import com.pawlowski.sportnite.presentation.view_models_related.sign_in_screen.SignInScreenUiState
import com.pawlowski.sportnite.presentation.view_models_related.sign_in_screen.SignInScreenViewModel
import org.orbitmvi.orbit.annotation.OrbitInternal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    viewModel: ISignInScreenViewModel = hiltViewModel<SignInScreenViewModel>(),
    onNavigateToEnterSignInCodeScreen: () -> Unit = {},
    onNavigateToWaitingForUserInfoScreen: () -> Unit = {},
)
{
    val uiState = viewModel.container.stateFlow.collectAsState()
    val phoneNumberInputState = remember {
        derivedStateOf {
            uiState.value.phoneNumberInput
        }
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { event ->
            when(event) {
                is SignInScreenSideEffect.NavigateToNextScreen -> {
                    onNavigateToEnterSignInCodeScreen()
                }
                is SignInScreenSideEffect.DisplayErrorToast -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_LONG).show()
                }
                is SignInScreenSideEffect.NavigateToSignedInScreen -> {
                    onNavigateToWaitingForUserInfoScreen()
                }
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(20.dp))
            LoginLottieAnimation()
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                text = "Witaj!"
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                text = "Zacznijmy, zaloguj się!"
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                value = phoneNumberInputState.value,
                onValueChange = { viewModel.changePhoneInput(it) },
                label = { Text(text = "Nr telefonu")}
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                text = "Na podany nr telefonu zostanie wysłany kod potwierdzający. Nie zostaną pobrane żadne opłaty, całość jest całkowicie darmowa",
            )
            Spacer(modifier = Modifier.height(30.dp))
            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp), onClick = { viewModel.sendVerificationCodeClick() }) {
                Text(text = "Wyślij potwierdzenie SMS")
            }


        }
    }
}

@OrbitInternal
@Preview(showBackground = true)
@Composable
fun SignInScreenPreview()
{
    SignInScreen(viewModel = object :
        OrbitMviPreviewViewModel<SignInScreenUiState, SignInScreenSideEffect>(),
        ISignInScreenViewModel {
        override fun stateForPreview(): SignInScreenUiState {
            return SignInScreenUiState(

            )
        }

        override fun changePhoneInput(newValue: String) {}

        override fun sendVerificationCodeClick() {}

    })
}