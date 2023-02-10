package com.pawlowski.user

import android.net.Uri
import com.pawlowski.auth.IAuthManager
import com.pawlowski.auth.cache.IUserInfoUpdateCache
import com.pawlowski.imageupload.IPhotoUploader
import com.pawlowski.models.AdvanceLevel
import com.pawlowski.models.Sport
import com.pawlowski.models.User
import com.pawlowski.models.params_models.UserUpdateInfoParams
import com.pawlowski.network.data.IGraphQLService
import com.pawlowski.notificationservice.synchronization.INotificationTokenSynchronizer
import com.pawlowski.utils.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class UserRepository @Inject constructor(
    private val userInfoUpdateCache: IUserInfoUpdateCache,
    private val authManager: IAuthManager,
    private val photoUploader: IPhotoUploader,
    private val ioDispatcher: CoroutineDispatcher,
    private val graphQLService: IGraphQLService,
    private val notificationTokenSynchronizer: INotificationTokenSynchronizer
): IUserRepository {
    override fun getUserSports(): Flow<UiData<List<Sport>>> {
        return userInfoUpdateCache.cachedLevels.map { sportsMap ->
            sportsMap?.keys?.toList()?.let {
                UiData.Success(isFresh = true, data = it)
            }?: kotlin.run {
                UiData.Error()
            }
        }
    }

    override fun getInfoAboutMe(): Flow<User?> {
        return userInfoUpdateCache.cachedUser
    }

    override fun signOut() {
        authManager.signOut()
        userInfoUpdateCache.deleteUserInfoCache()
        notificationTokenSynchronizer.deleteCurrentToken()
    }

    override suspend fun updateUserInfo(params: UserUpdateInfoParams): Resource<Unit> {
        val uploadedPhotoUri = params.photoUrl?.let {
            val result = photoUploader.uploadNewImage(
                Uri.parse(it),
                authManager.getCurrentUserUid()!!
            )
            result.dataOrNull()
        } ?: return Resource.Error(defaultRequestError)
        val result = graphQLService.updateUserInfo(params.copy(photoUrl = uploadedPhotoUri))

        return result.onSuccess {
            userInfoUpdateCache.markUserInfoAsSaved(
                User(
                    userName = params.name,
                    userPhotoUrl = uploadedPhotoUri,
                    userPhoneNumber = authManager.getUserPhone()
                )
            )
        }
    }

    override suspend fun updateAdvanceLevelInfo(levels: Map<Sport, AdvanceLevel>): Resource<Unit> {
        return withContext(ioDispatcher) {
            val isAllSuccess = levels.map {
                async {
                    graphQLService.updateAdvanceLevelInfo(it.toPair())
                }
            }.all {
                it.await() is Resource.Success
            }
            if(isAllSuccess)
            {
                userInfoUpdateCache.saveInfoAboutAdvanceLevels(levels)
                Resource.Success(Unit)
            }
            else
                Resource.Error(defaultRequestError)
        }
    }
}