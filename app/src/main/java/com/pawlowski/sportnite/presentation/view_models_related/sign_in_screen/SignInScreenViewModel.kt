package com.pawlowski.sportnite.presentation.view_models_related.sign_in_screen

import androidx.lifecycle.ViewModel
import com.pawlowski.sportnite.data.auth.AuthManager
import com.pawlowski.sportnite.data.auth.AuthResponse
import com.pawlowski.sportnite.utils.UiText
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
        initialState = SignInScreenUiState(),
        onCreate = {
            observeAuthState()
        }
    )

    override fun changePhoneInput(newValue: String) = intent {
        reduce {
            state.copy(phoneNumberInput = newValue)
        }
    }

    override fun sendVerificationCodeClick() = intent {
        val phone = state.phoneNumberInput
        if(phone.length < 6) {
            postSideEffect(SignInScreenSideEffect.DisplayErrorToast(UiText.NonTranslatable("Numer telefonu jest niepoprawny!")))
        }
        else if(!phone.matches(Regex("^[+][0-9]+"))) {
            postSideEffect(SignInScreenSideEffect.DisplayErrorToast(UiText.NonTranslatable("Numer telefonu musi zawierać na początku numer kierunkowy, np +48 dla Polski")))
        } else {
            reduce {
                state.copy(isLoading = true)
            }
            authManager.authenticate(phone)
        }



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
    }
}