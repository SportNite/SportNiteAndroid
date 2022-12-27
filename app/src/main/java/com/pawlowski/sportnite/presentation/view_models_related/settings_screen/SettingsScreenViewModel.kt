package com.pawlowski.sportnite.presentation.view_models_related.settings_screen

import androidx.lifecycle.ViewModel
import com.pawlowski.sportnite.presentation.ui.utils.getPlayerForPreview
import com.pawlowski.sportnite.presentation.use_cases.GetInfoAboutMeUseCase
import com.pawlowski.sportnite.presentation.use_cases.SignOutUseCase
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
            initialState = SettingsScreenUiState(null)
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

    init {
        observeInfoAboutMe()
    }
}