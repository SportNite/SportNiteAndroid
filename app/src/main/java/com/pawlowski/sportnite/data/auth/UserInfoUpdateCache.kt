package com.pawlowski.sportnite.data.auth

import android.content.SharedPreferences
import com.apollographql.apollo3.ApolloClient
import com.pawlowski.sportnite.MeQuery
import com.pawlowski.sportnite.data.mappers.getSportFromSportId
import com.pawlowski.sportnite.data.mappers.toAdvanceLevel
import com.pawlowski.sportnite.data.mappers.toSport
import com.pawlowski.sportnite.presentation.mappers.getAdvanceLevelFromParsedString
import com.pawlowski.sportnite.presentation.mappers.parse
import com.pawlowski.sportnite.presentation.models.AdvanceLevel
import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.sportnite.presentation.models.User
import com.pawlowski.sportnite.utils.Resource
import com.pawlowski.sportnite.utils.UiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInfoUpdateCache @Inject constructor(
    private val apolloClient: ApolloClient,
    private val sharedPreferences: SharedPreferences,
    private val ioDispatcher: CoroutineDispatcher
){
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

    val cachedUser get() = _cachedUser.asStateFlow()

    private val _cachedLevels: MutableStateFlow<Map<Sport, AdvanceLevel>?> by lazy {
        MutableStateFlow(getCachedLevelsFromPreferences())
    }
    val cachedLevels get() = _cachedLevels.asStateFlow()

    suspend fun didUserAddInfo(userPhoneNumber: String): Resource<RegistrationProgress> {
        return if(sharedPreferences.contains(USER_INFO_KEY) && sharedPreferences.contains(
                LEVELS_INFO_KEY)) {
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

    fun saveInfoAboutAdvanceLevels(levels: Map<Sport, AdvanceLevel>) {
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

    fun markUserInfoAsSaved(user: User) {
        sharedPreferences
            .edit()
            .putBoolean(USER_INFO_KEY, true)
            .putString(NAME_KEY, user.userName)
            .putString(PHOTO_URL_KEY, user.userPhotoUrl)
            .putString(PHONE_NUMBER_KEY, user.userPhoneNumber)
            .apply()
        _cachedUser.value = user
    }

    fun deleteUserInfoCache() {
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
            try {
                val response = apolloClient.query(MeQuery()).execute()
                val name = response.data!!.me.name
                val photoUrl = response.data!!.me.avatar
                val levels = response.data!!.me.skills.associate {
                    Pair(
                        it.sport.toSport(),
                        it.toAdvanceLevel()
                    )
                }
                val isProfileInfoAdded = name.isNotEmpty() && photoUrl.isNotEmpty()
                sharedPreferences
                    .edit()
                    .putBoolean(USER_INFO_KEY, isProfileInfoAdded)
                    .putString(NAME_KEY, name)
                    .putString(PHOTO_URL_KEY, photoUrl)
                    .putString(PHONE_NUMBER_KEY, userPhoneNumber)
                    .apply()
                if(isProfileInfoAdded) {
                    _cachedUser.value = User(name, photoUrl, userPhoneNumber)
                }
                if(levels.isNotEmpty()) {
                    saveInfoAboutAdvanceLevels(levels)
                }
                val result =
                    if(levels.isNotEmpty())
                        RegistrationProgress.EVERYTHING_ADDED
                    else if(isProfileInfoAdded)
                        RegistrationProgress.PROFILE_INFO_ADDED
                    else RegistrationProgress.NO_INFO_ADDED

                Resource.Success(result)
            }
            catch (e: Exception) {
                Resource.Error(UiText.NonTranslatable(""))
            }
        }
    }

    enum class RegistrationProgress {
        NO_INFO_ADDED,
        PROFILE_INFO_ADDED,
        EVERYTHING_ADDED
    }
}