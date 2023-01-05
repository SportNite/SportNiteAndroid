package com.pawlowski.sportnite.di

import com.pawlowski.sportnite.domain.AppRepository
import com.pawlowski.sportnite.domain.IAppRepository
import com.pawlowski.sportnite.presentation.use_cases.*
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

    @Singleton
    @Provides
    fun sendGameOfferToAcceptUseCase(appRepository: IAppRepository): SendGameOfferToAcceptUseCase = SendGameOfferToAcceptUseCase(appRepository::sendOfferToAccept)

    @Singleton
    @Provides
    fun updateUserInfoUseCase(appRepository: IAppRepository): UpdateUserInfoUseCase = UpdateUserInfoUseCase(appRepository::updateUserInfo)

    @Singleton
    @Provides
    fun getInfoAboutMeUseCase(appRepository: IAppRepository): GetInfoAboutMeUseCase = GetInfoAboutMeUseCase(appRepository::getInfoAboutMe)

    @Singleton
    @Provides
    fun getMyOffersUseCase(appRepository: IAppRepository): GetMyOffersUseCase = GetMyOffersUseCase(appRepository::getMyGameOffers)

    @Singleton
    @Provides
    fun deleteMyOfferUseCase(appRepository: IAppRepository): DeleteMyOfferUseCase = DeleteMyOfferUseCase(appRepository::deleteMyOffer)

    @Singleton
    @Provides
    fun getPlayersUseCase(appRepository: IAppRepository): GetPlayersUseCase = GetPlayersUseCase(appRepository::getPlayers)

    @Singleton
    @Provides
    fun signOutUseCase(appRepository: IAppRepository): SignOutUseCase = SignOutUseCase(appRepository::signOut)

    @Singleton
    @Provides
    fun acceptOfferToAcceptUseCase(appRepository: IAppRepository): AcceptOfferToAcceptUseCase = AcceptOfferToAcceptUseCase(appRepository::acceptOfferToAccept)

    @Singleton
    @Provides
    fun getUserSportsUseCase(appRepository: IAppRepository) = GetUserSportsUseCase(appRepository::getUserSports)

    @Singleton
    @Provides
    fun getPlayerDetailsUseCase(appRepository: IAppRepository) = GetPlayerDetailsUseCase(appRepository::getPlayerDetails)

    @Singleton
    @Provides
    fun getMeetingByIdUseCase(appRepository: IAppRepository) = GetMeetingByIdUseCase(appRepository::getMeetingDetails)

    @Singleton
    @Provides
    fun updateAdvanceLevelInfoUseCase(appRepository: IAppRepository) = UpdateAdvanceLevelInfoUseCase(appRepository::updateAdvanceLevelInfo)

    @Singleton
    @Provides
    fun getPagedOffersUseCase(appRepository: IAppRepository) = GetPagedOffersUseCase(appRepository::getPagedOffers)

    @Singleton
    @Provides
    fun getPagedMeetingsUseCase(appRepository: IAppRepository) = GetPagedMeetingsUseCase(appRepository::getPagedMeetings)
}