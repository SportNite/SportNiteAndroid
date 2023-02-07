package com.pawlowski.notificationservice.di

import com.google.firebase.messaging.FirebaseMessaging
import com.pawlowski.notificationservice.synchronization.INotificationTokenSynchronizer
import com.pawlowski.notificationservice.synchronization.NotificationTokenSynchronizer
import com.pawlowski.notificationservice.preferences.DeviceIdAndTokenPreferences
import com.pawlowski.notificationservice.preferences.IDeviceIdAndTokenPreferences
import com.pawlowski.notificationservice.worker.INotificationTokenSynchronizationWorkStarter
import com.pawlowski.notificationservice.worker.NotificationTokenSynchronizationWorkStarter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationServiceModule {
    @Singleton
    @Provides
    internal fun synchronizer(notificationTokenSynchronizer: NotificationTokenSynchronizer): INotificationTokenSynchronizer = notificationTokenSynchronizer

    @Singleton
    @Provides
    internal fun preferences(deviceIdAndTokenPreferences: DeviceIdAndTokenPreferences): IDeviceIdAndTokenPreferences = deviceIdAndTokenPreferences

    @Singleton
    @Provides
    fun firebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()

    @Singleton
    @Provides
    internal fun notificationTokenSynchronizationWorkStarter(ntsWorkStarter: NotificationTokenSynchronizationWorkStarter): INotificationTokenSynchronizationWorkStarter = ntsWorkStarter
}