package com.pawlowski.notifications.di

import com.pawlowski.notifications.INotificationsRepository
import com.pawlowski.notifications.NotificationsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationsModule {

    @Singleton
    @Provides
    internal fun notificationsRepository(notificationsRepository: NotificationsRepository): INotificationsRepository = notificationsRepository

}