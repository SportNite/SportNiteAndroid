package com.pawlowski.sportnite.data.auth

import android.content.SharedPreferences
import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.pawlowski.sportnite.MeQuery
import com.pawlowski.sportnite.presentation.models.User
import com.pawlowski.sportnite.utils.Resource
import com.pawlowski.sportnite.utils.UiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInfoUpdateCache @Inject constructor(
    private val apolloClient: ApolloClient,
    private val sharedPreferences: SharedPreferences,
    private val ioDispatcher: CoroutineDispatcher
){
    suspend fun didUserAddInfo(userId: String): Resource<Boolean> {
        return if(sharedPreferences.contains("userInfo")) {
            Resource.Success(sharedPreferences.getBoolean("userInfo", false))
        } else {
            checkValueFromApi()
        }
    }

    fun getCachedUserInfo(): User {
        TODO()
    }

    fun markUserInfoAsSaved() {
        sharedPreferences.edit().putBoolean("userInfo", true).apply()
    }

    fun deleteUserInfoCache() {
        sharedPreferences.edit().remove("userInfo").apply()
    }

    private suspend fun checkValueFromApi(): Resource<Boolean>
    {
        return withContext(ioDispatcher) {
            try {
                val response = apolloClient.query(MeQuery()).execute()
                val name = response.data!!.me.name
                val result = name.isNotEmpty()
                sharedPreferences.edit().putBoolean("userInfo", result).apply()
                Resource.Success(result)
            }
            catch (e: Exception) {
                Resource.Error(UiText.NonTranslatable(""))
            }
        }
    }
}