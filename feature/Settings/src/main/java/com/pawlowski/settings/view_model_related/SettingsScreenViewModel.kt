package com.pawlowski.settings.view_model_related

import androidx.lifecycle.ViewModel
import com.pawlowski.user.use_cases.GetInfoAboutMeUseCase
import com.pawlowski.user.use_cases.SignOutUseCase
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
class SettingsScreenViewModel @Inject constructor(
    private val getInfoAboutMeUseCase: GetInfoAboutMeUseCase,
    private val signOutUseCase: SignOutUseCase
): ISettingsScreenViewModel, ViewModel() {
    override val container: Container<SettingsScreenUiState, SettingsScreenSideEffect> =
        container(
            initialState = SettingsScreenUiState(null),
            onCreate = {
                observeInfoAboutMe()
            }
        )

    private fun observeInfoAboutMe() = intent(registerIdling = false) {
        repeatOnSubscription {
            getInfoAboutMeUseCase().collectLatest {
                reduce {
                    state.copy(player = it)
                }
            }
        }
    }

    override fun signOut() = intent {
        signOutUseCase()
        postSideEffect(SettingsScreenSideEffect.NavigateToLoginScreen)
    }


}