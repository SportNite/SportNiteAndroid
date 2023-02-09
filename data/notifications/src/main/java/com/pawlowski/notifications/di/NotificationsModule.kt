package com.pawlowski.notifications.di

import com.pawlowski.notifications.INotificationsRepository
import com.pawlowski.notifications.NotificationsRepository
import com.pawlowski.notifications.use_cases.GetPagedNotificationsUseCase
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

    @Singleton
    @Provides
    fun getPagedNotificationsUseCase(appRepository: INotificationsRepository) = GetPagedNotificationsUseCase(appRepository::getPagedNotifications)

}