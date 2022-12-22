package com.pawlowski.sportnite.di

import com.pawlowski.sportnite.domain.IAppRepository
import com.pawlowski.sportnite.presentation.use_cases.AddGameOfferUseCase
import com.pawlowski.sportnite.presentation.use_cases.GetGameOffersUseCase
import com.pawlowski.sportnite.presentation.use_cases.GetIncomingMeetingsUseCase
import com.pawlowski.sportnite.presentation.use_cases.GetOffersToAcceptUseCase
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

    @Singleton
    @Provides
    fun getGameOffersUseCase(appRepository: IAppRepository): GetGameOffersUseCase = GetGameOffersUseCase(appRepository::getGameOffers)

    @Singleton
    @Provides
    fun getOffersToAcceptUseCase(appRepository: IAppRepository): GetOffersToAcceptUseCase = GetOffersToAcceptUseCase(appRepository::getOffersToAccept)

    @Singleton
    @Provides
    fun getIncomingMeetingsUseCase(appRepository: IAppRepository): GetIncomingMeetingsUseCase = GetIncomingMeetingsUseCase(appRepository::getIncomingMeetings)
}