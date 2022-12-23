package com.pawlowski.sportnite.presentation.view_models_related.sign_in_screen

import androidx.lifecycle.ViewModel
import com.pawlowski.sportnite.data.auth.AuthManager
import com.pawlowski.sportnite.data.auth.AuthResponse
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
class SignInScreenViewModel @Inject constructor(
    private val authManager: AuthManager,
): ISignInScreenViewModel, ViewModel() {
    override val container: Container<SignInScreenUiState, SignInScreenSideEffect> = container(
        SignInScreenUiState()
    )

    override fun changePhoneInput(newValue: String) = intent {
        reduce {
            state.copy(phoneNumberInput = newValue)
        }
    }

    override fun sendVerificationCodeClick() = intent {
        reduce {
            state.copy(isLoading = true)
        }
        authManager.authenticate(state.phoneNumberInput)

    }

    private fun observeAuthState() = intent(registerIdling = false) {
        repeatOnSubscription {
            authManager.signUpState.collectLatest {
                when(it) {
                    is AuthResponse.Success -> {
                        postSideEffect(SignInScreenSideEffect.NavigateToSignedInScreen)

                    }
                    is AuthResponse.Error -> {
                        //TODO
                    }
                    is AuthResponse.NotInitialized -> {
                        //TODO
                    }
                    is AuthResponse.Loading -> {
                        reduce {
                            state.copy(isLoading = false)
                        }
                        postSideEffect(SignInScreenSideEffect.NavigateToNextScreen)
                    }
                }
            }
        }
    }

    init {
        observeAuthState()
    }
}