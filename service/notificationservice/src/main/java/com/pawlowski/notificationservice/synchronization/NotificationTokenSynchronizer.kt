package com.pawlowski.notificationservice.synchronization

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.pawlowski.network.data.IGraphQLService
import com.pawlowski.notificationservice.preferences.DeviceIdAndTokenPreferences
import com.pawlowski.utils.Resource
import com.pawlowski.utils.onError
import com.pawlowski.utils.onSuccess
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NotificationTokenSynchronizer @Inject constructor(
    private val deviceIdAndTokenPreferences: DeviceIdAndTokenPreferences,
    private val graphqlService: IGraphQLService,
    private val firebaseMessaging: FirebaseMessaging
) : INotificationTokenSynchronizer {

    override suspend fun synchronizeWithServer(newToken: String): Resource<Unit> {
        return if (!isThisTokenSynchronizedWithServer(newToken)) {
            val deviceId = getOrCreateDeviceId()

            val result = graphqlService.sendNotificationToken(
                token = newToken,
                deviceId = deviceId
            )

            result.onSuccess {
                deviceIdAndTokenPreferences.updateDeviceIdAndToken {
                    it.copy(token = newToken)
                }
                Log.d("NotificationTokenSynchronizer", "Success")
            }.onError { _, _ ->
                Log.d("NotificationTokenSynchronizer", "Error")
            }
        }
        else
            Resource.Success(Unit)

    }

    override fun deleteCurrentToken() {
        synchronized(this) {
            firebaseMessaging.deleteToken()
            deviceIdAndTokenPreferences.updateDeviceIdAndToken {
                it.copy(token = null)
            }
        }

    }

    private fun getOrCreateDeviceId(): String {
        val currentValue = deviceIdAndTokenPreferences.getDeviceIdAndToken()
        return if (currentValue.deviceId.isNullOrEmpty()) {
            val newId = UUID.randomUUID().toString()
            deviceIdAndTokenPreferences.updateDeviceIdAndToken {
                it.copy(deviceId = newId)
            }
            newId
        } else
            currentValue.deviceId

    }

    private fun isThisTokenSynchronizedWithServer(token: String): Boolean {
        val currentValue = deviceIdAndTokenPreferences.getDeviceIdAndToken()
        return currentValue.token == token
    }
}