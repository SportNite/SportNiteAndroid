package com.pawlowski.cache

import com.pawlowski.models.AdvanceLevel
import com.pawlowski.models.Sport
import com.pawlowski.models.User
import com.pawlowski.utils.Resource
import kotlinx.coroutines.flow.StateFlow

interface IUserInfoUpdateCache {
    val cachedUser: StateFlow<User?>
    val cachedLevels: StateFlow<Map<Sport, AdvanceLevel>?>

    suspend fun didUserAddInfo(userPhoneNumber: String): Resource<RegistrationProgress>
    fun saveInfoAboutAdvanceLevels(levels: Map<Sport, AdvanceLevel>)
    fun markUserInfoAsSaved(user: User)
    fun deleteUserInfoCache()

}