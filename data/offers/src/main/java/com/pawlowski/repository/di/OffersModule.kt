package com.pawlowski.repository.di

import com.pawlowski.repository.IOffersRepository
import com.pawlowski.repository.OffersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OffersModule {

    @Singleton
    @Provides
    internal fun offersRepository(offersRepository: OffersRepository): IOffersRepository = offersRepository
}