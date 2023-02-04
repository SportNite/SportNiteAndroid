package com.pawlowski.sportnite.presentation.view_models_related.waiting_for_user_info_screen

import androidx.lifecycle.ViewModel
import com.pawlowski.sportnite.data.auth.AuthManager
import com.pawlowski.sportnite.data.auth.UserInfoUpdateCache
import com.pawlowski.sportnite.data.auth.UserInfoUpdateCache.RegistrationProgress.*
import com.pawlowski.utils.onError
import com.pawlowski.utils.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class WaitingForUserInfoViewModel @Inject constructor(
    private val userInfoUpdateCache: UserInfoUpdateCache,
    private val authManager: AuthManager
):IWaitingForUserInfoViewModel, ViewModel() {

    override val container: Container<WaitingForUserInfoUiState, WaitingForUserInfoSideEffect> = container(
        initialState = WaitingForUserInfoUiState(
            message = "Initializing...",
            isLoading = false,
        )
    )

    override fun checkUserInfo() = intent {
        if(state.isLoading)
            return@intent

        reduce {
            state.copy(isLoading = true, message = "Please wait...")
        }
        val info = userInfoUpdateCache.didUserAddInfo(authManager.getUserPhone())
        info.onSuccess {
            when (it) {
                EVERYTHING_ADDED -> {
                    postSideEffect(WaitingForUserInfoSideEffect.NavigateToHomeScreen)
                    reduce {
                        state.copy(isLoading = false, message = "Please wait...")
                    }
                }
                NO_INFO_ADDED -> {
                    postSideEffect(WaitingForUserInfoSideEffect.NavigateToAccountDetailsScreen)
                    reduce {
                        state.copy(isLoading = false, message = "Please wait...")
                    }
                }
                PROFILE_INFO_ADDED -> {
                    postSideEffect(WaitingForUserInfoSideEffect.NavigateToChooseSportsScreen)
                    reduce {
                        state.copy(isLoading = false, message = "Please wait...")
                    }
                }
                else -> {}
            }
        }.onError { _, _ ->
            reduce {
                state.copy(message = "Some error occurred", isLoading = false)
            }
        }
    }

    /*init {
        checkUserInfo()
    }*/
}