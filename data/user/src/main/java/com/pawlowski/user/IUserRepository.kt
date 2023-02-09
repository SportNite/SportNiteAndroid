package com.pawlowski.user

import com.pawlowski.models.AdvanceLevel
import com.pawlowski.models.Sport
import com.pawlowski.models.User
import com.pawlowski.models.params_models.UserUpdateInfoParams
import com.pawlowski.utils.Resource
import com.pawlowski.utils.UiData
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun getUserSports(): Flow<UiData<List<Sport>>>
    fun getInfoAboutMe() : Flow<User?>
    suspend fun updateUserInfo(params: UserUpdateInfoParams): Resource<Unit>
    suspend fun updateAdvanceLevelInfo(levels: Map<Sport, AdvanceLevel>): Resource<Unit>

    fun signOut()
}