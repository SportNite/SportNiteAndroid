package com.pawlowski.sportnite.presentation.view_models_related.waiting_for_user_info_screen

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.pawlowski.sportnite.data.auth.AuthManager
import com.pawlowski.sportnite.data.auth.UserInfoUpdateCache
import com.pawlowski.sportnite.utils.Resource
import com.pawlowski.sportnite.utils.onError
import com.pawlowski.sportnite.utils.onSuccess
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
            message = "Please wait...",
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
            if(it == true)
            {
                postSideEffect(WaitingForUserInfoSideEffect.NavigateToHomeScreen)
                reduce {
                    state.copy(isLoading = false, message = "Please wait...")
                }
            }
            else if(it == false) {
                postSideEffect(WaitingForUserInfoSideEffect.NavigateToAccountDetailsScreen)
                reduce {
                    state.copy(isLoading = false, message = "Please wait...")
                }
            }
        }.onError { _, _ ->
            reduce {
                state.copy(message = "Some error occured", isLoading = false)
            }
        }
    }

    init {
        checkUserInfo()
    }
}