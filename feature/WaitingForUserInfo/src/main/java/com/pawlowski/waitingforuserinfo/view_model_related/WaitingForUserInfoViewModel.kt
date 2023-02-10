package com.pawlowski.waitingforuserinfo.view_model_related

import androidx.lifecycle.ViewModel
import com.pawlowski.auth.ILightAuthManager
import com.pawlowski.auth.cache.IUserInfoUpdateCache
import com.pawlowski.auth.cache.RegistrationProgress
import com.pawlowski.notificationservice.worker.INotificationTokenSynchronizationWorkStarter
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
    private val userInfoUpdateCache: IUserInfoUpdateCache,
    private val authManager: ILightAuthManager,
    private val notificationTokenSynchronizationWorkStarter: INotificationTokenSynchronizationWorkStarter
): IWaitingForUserInfoViewModel, ViewModel() {

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

        notificationTokenSynchronizationWorkStarter.startWorker()
        val info = userInfoUpdateCache.didUserAddInfo(authManager.getUserPhone())
        info.onSuccess {
            when (it) {
                RegistrationProgress.EVERYTHING_ADDED -> {
                    postSideEffect(WaitingForUserInfoSideEffect.NavigateToHomeScreen)
                    reduce {
                        state.copy(isLoading = false, message = "Please wait...")
                    }
                }
                RegistrationProgress.NO_INFO_ADDED -> {
                    postSideEffect(WaitingForUserInfoSideEffect.NavigateToAccountDetailsScreen)
                    reduce {
                        state.copy(isLoading = false, message = "Please wait...")
                    }
                }
                RegistrationProgress.PROFILE_INFO_ADDED -> {
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