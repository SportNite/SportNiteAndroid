package com.pawlowski.authentication.ui.enter_sign_in_code_screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pawlowski.sharedresources.R
import com.pawlowski.commonui.LoginLottieAnimation
import com.pawlowski.commonui.TextDivider
import com.pawlowski.commonui.utils.OrbitMviPreviewViewModel
import com.pawlowski.authentication.view_model_related.enter_sign_in_code_screen.EnterSignInCodeScreenViewModel
import com.pawlowski.authentication.view_model_related.enter_sign_in_code_screen.EnterSignInCodeSideEffect
import com.pawlowski.authentication.view_model_related.enter_sign_in_code_screen.EnterSignInCodeUiState
import com.pawlowski.authentication.view_model_related.enter_sign_in_code_screen.IEnterSignInCodeScreenViewModel
import org.orbitmvi.orbit.annotation.OrbitInternal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EnterSignInCodeScreen(
    viewModel: IEnterSignInCodeScreenViewModel = hiltViewModel<EnterSignInCodeScreenViewModel>(),
    onNavigateToNextScreen: () -> Unit = {},
    onNavigateToHomeScreen: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    ) {
    val uiState = viewModel.container.stateFlow.collectAsState()
    val codeInputState = remember {
        derivedStateOf {
            uiState.value.codeInput
        }
    }
    val phoneNumberState = remember {
        derivedStateOf {
            uiState.value.phoneNumber
        }
    }
    val isSendAgainAvailableState = remember {
        derivedStateOf {
            uiState.value.isSendAgainAvailable
        }
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { event ->
            when(event) {
                is EnterSignInCodeSideEffect.MoveToHomeScreen -> {
                    onNavigateToHomeScreen()
                }
                is EnterSignInCodeSideEffect.MoveToAccountDetailsScreen -> {
                    onNavigateToNextScreen()
                }
                is EnterSignInCodeSideEffect.ShowErrorToast -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            IconButton(onClick = { onNavigateBack() }) {
                Icon(painter = painterResource(id = R.drawable.back_icon), contentDescription = "")
            }
            LoginLottieAnimation()
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                text = "Potwierdź kod"
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                text = "Aby kontynuować, podaj kod z SMS"
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                value = codeInputState.value,
                onValueChange = { viewModel.changeCodeInput(it) },
                label = { Text(text = "Kod z SMS") }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                text = "Kod nie doszedł lub stracił ważność? Poczekaj chwilę lub spróbuj ponownie",
            )
            Spacer(modifier = Modifier.height(30.dp))
            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp), onClick = { viewModel.confirmCodeClick() }) {
                Text(text = "Potwierdź")
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextDivider(text = "Lub wyślij kod ponownie")

            Spacer(modifier = Modifier.height(20.dp))

            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
                onClick = { viewModel.sendVerificationCodeAgainClick() },
                enabled = isSendAgainAvailableState.value
            ) {
                Text(text = "Wyślij kod jeszcze raz")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                text = "Kod zostanie wysłany na nr telefonu ${phoneNumberState.value}. Coś się nie zgadza? Wróć i podaj nr telefonu jeszcze raz",
            )
        }
    }
}

@OrbitInternal
@Preview(showBackground = true)
@Composable
private fun EnterSignInCodeScreenPreview() {
    EnterSignInCodeScreen(object :
        OrbitMviPreviewViewModel<EnterSignInCodeUiState, EnterSignInCodeSideEffect>(),
        IEnterSignInCodeScreenViewModel
    {
        override fun stateForPreview(): EnterSignInCodeUiState {
            return EnterSignInCodeUiState(phoneNumber = "762821051")
        }
        override fun changeCodeInput(newValue: String) {}
        override fun sendVerificationCodeAgainClick() {}
        override fun confirmCodeClick() {}

    })
}