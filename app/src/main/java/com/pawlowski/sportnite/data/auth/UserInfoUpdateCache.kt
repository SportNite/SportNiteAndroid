package com.pawlowski.sportnite.data.auth

import android.content.SharedPreferences
import com.apollographql.apollo3.ApolloClient
import com.pawlowski.sportnite.MeQuery
import com.pawlowski.sportnite.presentation.models.User
import com.pawlowski.sportnite.utils.Resource
import com.pawlowski.sportnite.utils.UiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    }

    private val _cachedUser: MutableStateFlow<User?> by lazy {
        MutableStateFlow(getCachedUserFromPreferences())
    }

    val cachedUser: StateFlow<User?> get() = _cachedUser

    suspend fun didUserAddInfo(userPhoneNumber: String): Resource<Boolean> {
        return if(sharedPreferences.contains(USER_INFO_KEY)) { //TODO: refactor to user StateFlow
            Resource.Success(sharedPreferences.getBoolean(USER_INFO_KEY, false))
        } else {
            checkValueFromApi(userPhoneNumber)
        }
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
        sharedPreferences
            .edit()
            .remove(USER_INFO_KEY)
            .remove(NAME_KEY)
            .remove(PHOTO_URL_KEY)
            .remove(PHONE_NUMBER_KEY)
            .apply()
    }

    private suspend fun checkValueFromApi(userPhoneNumber: String): Resource<Boolean>
    {
        return withContext(ioDispatcher) {
            try {
                val response = apolloClient.query(MeQuery()).execute()
                val name = response.data!!.me.name
                val photoUrl = response.data!!.me.avatar
                val result = name.isNotEmpty() && photoUrl.isNotEmpty()
                sharedPreferences
                    .edit()
                    .putBoolean(USER_INFO_KEY, result)
                    .putString(NAME_KEY, name)
                    .putString(PHOTO_URL_KEY, photoUrl)
                    .putString(PHONE_NUMBER_KEY, userPhoneNumber)
                    .apply()
                if(result) {
                    _cachedUser.value = User(name, photoUrl, userPhoneNumber)
                }
                Resource.Success(result)
            }
            catch (e: Exception) {
                Resource.Error(UiText.NonTranslatable(""))
            }
        }
    }
}