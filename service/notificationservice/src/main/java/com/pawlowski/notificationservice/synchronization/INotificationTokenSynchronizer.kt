package com.pawlowski.notificationservice.synchronization

import com.pawlowski.utils.Resource

interface INotificationTokenSynchronizer {
    suspend fun synchronizeWithServer(newToken: String): Resource<Unit>
    fun deleteCurrentToken()
}