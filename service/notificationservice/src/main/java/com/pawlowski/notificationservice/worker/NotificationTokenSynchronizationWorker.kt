package com.pawlowski.notificationservice.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.google.firebase.messaging.FirebaseMessaging
import com.pawlowski.auth.ILightAuthManager
import com.pawlowski.notificationservice.channel_handler.INotificationChannelHandler
import com.pawlowski.notificationservice.synchronization.INotificationTokenSynchronizer
import com.pawlowski.utils.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await

/**
 * Updates token if user is authenticated and token isn't already updated
 */
@HiltWorker
internal class NotificationTokenSynchronizationWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val firebaseMessaging: FirebaseMessaging,
    private val notificationTokenHandler: INotificationTokenSynchronizer,
    private val authManager: ILightAuthManager,
    private val notificationChannelHandler: INotificationChannelHandler,
): CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val NOTIFICATION_ID = 743
        private const val CHANNEL_ID = "Synchronization"
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NOTIFICATION_ID, createNotification()
        )
    }

    override suspend fun doWork(): Result {
        notificationChannelHandler.innitNotificationChannels()
        return if(authManager.isUserAuthenticated()) {
            val token = firebaseMessaging.token.await()
            when(notificationTokenHandler.synchronizeWithServer(token)) {
                is Resource.Success -> Result.success()
                is Resource.Error -> Result.retry()
            }
        }
        else
            Result.failure()
    }

    private fun createNotification() : Notification {
        createNotificationChannel()

        return NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setContentTitle("Synchronization with server")
            .setContentText("Synchronization with server is happening...")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel() {
        val name = "Synchronization"
        val descriptionText = "Displayed when data is being synchronized with server"
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}