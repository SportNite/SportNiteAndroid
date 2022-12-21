package com.pawlowski.sportnite.di

import com.pawlowski.sportnite.domain.AppRepository
import com.pawlowski.sportnite.domain.IAppRepository
import com.pawlowski.sportnite.presentation.use_cases.AddGameOfferUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCasesAppModule {

    @Singleton
    @Provides
    fun AddGameOfferUseCase(appRepository: IAppRepository): AddGameOfferUseCase = AddGameOfferUseCase(appRepository::addGameOffer)


}