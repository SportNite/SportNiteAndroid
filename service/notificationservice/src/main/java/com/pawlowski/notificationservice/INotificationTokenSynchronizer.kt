package com.pawlowski.notificationservice

import com.pawlowski.utils.Resource

interface INotificationTokenSynchronizer {
    suspend fun synchronizeWithServer(newToken: String): Resource<Unit>
}