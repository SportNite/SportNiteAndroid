package com.pawlowski.notificationservice

import com.pawlowski.network.data.IGraphQLService
import com.pawlowski.notificationservice.preferences.DeviceIdAndTokenPreferences
import com.pawlowski.utils.Resource
import com.pawlowski.utils.onSuccess
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NotificationTokenSynchronizer @Inject constructor(
    private val deviceIdAndTokenPreferences: DeviceIdAndTokenPreferences,
    private val graphqlService: IGraphQLService
) : INotificationTokenSynchronizer {

    override suspend fun synchronizeWithServer(newToken: String): Resource<Unit> {

        return if(!isThisTokenSynchronizedWithServer(newToken)) {
            val deviceId = getOrCreateDeviceId()

            val result = graphqlService.sendNotificationToken(
                token = newToken,
                deviceId = deviceId
            ) //TODO: Think what to do with userUid

            result.onSuccess {
                deviceIdAndTokenPreferences.updateDeviceIdAndToken {
                    it.copy(token = newToken)
                }
            }
        }
        else
            Resource.Success(Unit)

    }

    private fun getOrCreateDeviceId(): String {
        val currentValue = deviceIdAndTokenPreferences.getDeviceIdAndToken()
        return if(currentValue.deviceId.isNullOrEmpty()) {
            val newId = UUID.randomUUID().toString()
            deviceIdAndTokenPreferences.updateDeviceIdAndToken {
                it.copy(deviceId = newId)
            }
            newId
        }
        else
            currentValue.deviceId

    }

    private fun isThisTokenSynchronizedWithServer(token: String): Boolean {
        val currentValue = deviceIdAndTokenPreferences.getDeviceIdAndToken()
        return currentValue.token == token
    }
}