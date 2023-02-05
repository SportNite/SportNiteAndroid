package com.pawlowski.cache

import android.content.SharedPreferences
import com.pawlowski.models.AdvanceLevel
import com.pawlowski.models.Sport
import com.pawlowski.models.User
import com.pawlowski.models.mappers.getAdvanceLevelFromParsedString
import com.pawlowski.models.mappers.getSportFromSportId
import com.pawlowski.models.mappers.parse
import com.pawlowski.network.data.IGraphQLService
import com.pawlowski.utils.Resource
import com.pawlowski.utils.UiText
import com.pawlowski.utils.dataOrNull
import com.pawlowski.utils.messageOrNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class UserInfoUpdateCache @Inject constructor(
    private val graphQLService: IGraphQLService,
    private val sharedPreferences: SharedPreferences,
    private val ioDispatcher: CoroutineDispatcher
) : IUserInfoUpdateCache {
    private companion object {
        const val USER_INFO_KEY = "userInfo"
        const val NAME_KEY = "name"
        const val PHOTO_URL_KEY = "photoUrl"
        const val PHONE_NUMBER_KEY = "phoneNumber"
        const val LEVELS_INFO_KEY = "levelsInfo"
        const val SPORT_IDS_KEY = "sportIds"
    }

    private val _cachedUser: MutableStateFlow<User?> by lazy {
        MutableStateFlow(getCachedUserFromPreferences())
    }

    override val cachedUser get() = _cachedUser.asStateFlow()

    private val _cachedLevels: MutableStateFlow<Map<Sport, AdvanceLevel>?> by lazy {
        MutableStateFlow(getCachedLevelsFromPreferences())
    }
    override val cachedLevels get() = _cachedLevels.asStateFlow()

    override suspend fun didUserAddInfo(userPhoneNumber: String): Resource<RegistrationProgress> {
        return if(sharedPreferences.contains(USER_INFO_KEY) && sharedPreferences.contains(
                LEVELS_INFO_KEY
            )) {
            val cachedUserValue = _cachedUser.value
            val cachedLevelsValue = _cachedLevels.value
            val result = if(cachedLevelsValue != null)
                RegistrationProgress.EVERYTHING_ADDED
            else if(cachedUserValue != null)
                RegistrationProgress.PROFILE_INFO_ADDED
            else
                RegistrationProgress.NO_INFO_ADDED
            Resource.Success(result)
        } else {
            checkValueFromApi(userPhoneNumber)
        }
    }

    private fun getAdvanceLevelFromCache(sportId: String): AdvanceLevel? {
        return try {
            getAdvanceLevelFromParsedString(sharedPreferences.getString("level:$sportId", "")!!)
        }
        catch (e: Exception) {
            null
        }
    }

    private fun getCachedLevelsFromPreferences(): Map<Sport, AdvanceLevel>? {
        return if(sharedPreferences.getBoolean(LEVELS_INFO_KEY,false))
        {
            val sportIds = sharedPreferences.getStringSet("sportIds", setOf())
            sportIds?.mapNotNull {
                getAdvanceLevelFromCache(it)?.let { level ->
                    Pair(getSportFromSportId(it), level)
                }
            }?.toMap()
        }
        else
        {
            null
        }
    }

    override fun saveInfoAboutAdvanceLevels(levels: Map<Sport, AdvanceLevel>) {
        sharedPreferences.edit()
            .putStringSet(SPORT_IDS_KEY, levels.keys.map { it.sportId }.toSet())
            .apply {
                levels.forEach { (sport, level) ->
                    putString("level:${sport.sportId}", level.parse())
                }
                if(levels.isNotEmpty())
                    putBoolean(LEVELS_INFO_KEY, true)
            }
            .apply()
        _cachedLevels.value = levels
    }

    private fun getCachedUserFromPreferences() : User? {
        val doesExist = sharedPreferences.getBoolean(USER_INFO_KEY, false)
        return if(!doesExist)
            null
        else
        {
            User(
                userName = sharedPreferences.getString(NAME_KEY, "")?:"",
                userPhotoUrl = sharedPreferences.getString(PHOTO_URL_KEY, "")?:"",
                userPhoneNumber = sharedPreferences.getString(PHONE_NUMBER_KEY, "")?:"",
            )
        }
    }

    override fun markUserInfoAsSaved(user: User) {
        sharedPreferences
            .edit()
            .putBoolean(USER_INFO_KEY, true)
            .putString(NAME_KEY, user.userName)
            .putString(PHOTO_URL_KEY, user.userPhotoUrl)
            .putString(PHONE_NUMBER_KEY, user.userPhoneNumber)
            .apply()
        _cachedUser.value = user
    }

    override fun deleteUserInfoCache() {
        _cachedUser.value = null
        _cachedLevels.value = null
        sharedPreferences
            .edit()
            .remove(USER_INFO_KEY)
            .remove(NAME_KEY)
            .remove(PHOTO_URL_KEY)
            .remove(PHONE_NUMBER_KEY)
            .remove(LEVELS_INFO_KEY)
            .remove(SPORT_IDS_KEY)
            .apply()
    }

    private suspend fun checkValueFromApi(userPhoneNumber: String): Resource<RegistrationProgress>
    {
        return withContext(ioDispatcher) {
            val result = graphQLService.getInfoAboutMe()
            result.dataOrNull()?.let {
                val isProfileInfoAdded = it.playerName.isNotEmpty() && it.playerPhotoUrl.isNotEmpty()

                sharedPreferences
                    .edit()
                    .putBoolean(USER_INFO_KEY, isProfileInfoAdded)
                    .putString(NAME_KEY, it.playerName)
                    .putString(PHOTO_URL_KEY, it.playerPhotoUrl)
                    .putString(PHONE_NUMBER_KEY, userPhoneNumber)
                    .apply()

                if(isProfileInfoAdded) {
                    _cachedUser.value = User(it.playerName, it.playerPhotoUrl, userPhoneNumber)
                }

                if(it.advanceLevels.isNotEmpty()) {
                    saveInfoAboutAdvanceLevels(it.advanceLevels)
                }

                val progress = when {
                    it.advanceLevels.isNotEmpty() -> RegistrationProgress.EVERYTHING_ADDED
                    isProfileInfoAdded -> RegistrationProgress.PROFILE_INFO_ADDED
                    else -> RegistrationProgress.NO_INFO_ADDED
                }

                Resource.Success(progress)

            }?: Resource.Error(result.messageOrNull()?: UiText.NonTranslatable(""))

        }
    }

}