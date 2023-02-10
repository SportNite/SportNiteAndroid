package com.pawlowski.authentication.view_model_related.enter_sign_in_code_screen

import androidx.lifecycle.ViewModel
import com.pawlowski.auth.IAuthManager
import com.pawlowski.utils.AuthResponse
import com.pawlowski.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
internal class EnterSignInCodeScreenViewModel @Inject constructor(
    private val authManager: IAuthManager,
): IEnterSignInCodeScreenViewModel, ViewModel() {
    override val container: Container<EnterSignInCodeUiState, EnterSignInCodeSideEffect> = container(
        initialState = EnterSignInCodeUiState(phoneNumber = "33333333"),
        onCreate = {
            observeAuthState()
        }
    )

    override fun changeCodeInput(newValue: String) = intent {
        reduce {
            state.copy(codeInput = newValue)
        }
    }

    override fun sendVerificationCodeAgainClick() {
        TODO("Not yet implemented")
    }

    override fun confirmCodeClick() = intent {
        try {
            authManager.onVerifyOtp(state.codeInput)
        }
        catch (e: Exception) {
            postSideEffect(EnterSignInCodeSideEffect.ShowErrorToast(UiText.NonTranslatable("Wystąpił błąd uwierzytelniania")))
        }

    }

    private fun observeAuthState() = intent(registerIdling = false) {
        repeatOnSubscription {
            authManager.signUpState.collectLatest {
                when(it) {
                    is AuthResponse.Success -> {
                        postSideEffect(EnterSignInCodeSideEffect.MoveToAccountDetailsScreen)
                    }
                    is AuthResponse.Error -> {
                        //TODO
                    }
                    is AuthResponse.NotInitialized -> {
                        //TODO
                    }
                    is AuthResponse.Loading -> {
                        //TODO
                    }
                }
            }
        }
    }


}