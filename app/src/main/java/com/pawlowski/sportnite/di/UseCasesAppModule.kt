package com.pawlowski.sportnite.di

import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.fresh
import com.pawlowski.meetings.IMeetingsRepository
import com.pawlowski.sportnite.domain.IAppRepository
import com.pawlowski.models.params_models.MeetingsFilter
import com.pawlowski.models.params_models.OffersFilter
import com.pawlowski.models.params_models.PlayersFilter
import com.pawlowski.models.GameOffer
import com.pawlowski.models.GameOfferToAccept
import com.pawlowski.models.Meeting
import com.pawlowski.models.Player
import com.pawlowski.notifications.INotificationsRepository
import com.pawlowski.players.IPlayersRepository
import com.pawlowski.repository.IOffersRepository
import com.pawlowski.responses.IResponsesRepository
import com.pawlowski.sportnite.presentation.use_cases.*
import com.pawlowski.user.IUserRepository
import com.pawlowski.utils.Resource
import com.pawlowski.utils.UiText
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
    fun AddGameOfferUseCase(appRepository: IOffersRepository): AddGameOfferUseCase = AddGameOfferUseCase(appRepository::addGameOffer)

    @Singleton
    @Provides
    fun getGameOffersUseCase(appRepository: IOffersRepository): GetGameOffersUseCase = GetGameOffersUseCase(appRepository::getGameOffers)

    @Singleton
    @Provides
    fun getOffersToAcceptUseCase(appRepository: IResponsesRepository): GetOffersToAcceptUseCase = GetOffersToAcceptUseCase(appRepository::getOffersToAccept)

    @Singleton
    @Provides
    fun getIncomingMeetingsUseCase(appRepository: IMeetingsRepository): GetIncomingMeetingsUseCase = GetIncomingMeetingsUseCase(appRepository::getIncomingMeetings)

    @Singleton
    @Provides
    fun sendGameOfferToAcceptUseCase(appRepository: IResponsesRepository): SendGameOfferToAcceptUseCase = SendGameOfferToAcceptUseCase(appRepository::sendOfferToAccept)

    @Singleton
    @Provides
    fun updateUserInfoUseCase(appRepository: IUserRepository): UpdateUserInfoUseCase = UpdateUserInfoUseCase(appRepository::updateUserInfo)

    @Singleton
    @Provides
    fun getInfoAboutMeUseCase(appRepository: IUserRepository): GetInfoAboutMeUseCase = GetInfoAboutMeUseCase(appRepository::getInfoAboutMe)

    @Singleton
    @Provides
    fun getMyOffersUseCase(appRepository: IOffersRepository): GetMyOffersUseCase = GetMyOffersUseCase(appRepository::getMyGameOffers)

    @Singleton
    @Provides
    fun deleteMyOfferUseCase(appRepository: IOffersRepository): DeleteMyOfferUseCase = DeleteMyOfferUseCase(appRepository::deleteMyOffer)

    @Singleton
    @Provides
    fun getPlayersUseCase(appRepository: IPlayersRepository): GetPlayersUseCase = GetPlayersUseCase(appRepository::getPlayers)

    @Singleton
    @Provides
    fun signOutUseCase(appRepository: IUserRepository): SignOutUseCase = SignOutUseCase(appRepository::signOut)

    @Singleton
    @Provides
    fun acceptOfferToAcceptUseCase(appRepository: IResponsesRepository): AcceptOfferToAcceptUseCase = AcceptOfferToAcceptUseCase(appRepository::acceptOfferToAccept)

    @Singleton
    @Provides
    fun getUserSportsUseCase(appRepository: IUserRepository) = GetUserSportsUseCase(appRepository::getUserSports)

    @Singleton
    @Provides
    fun getPlayerDetailsUseCase(appRepository: IPlayersRepository) = GetPlayerDetailsUseCase(appRepository::getPlayerDetails)

    @Singleton
    @Provides
    fun getMeetingByIdUseCase(appRepository: IMeetingsRepository) = GetMeetingByIdUseCase(appRepository::getMeetingDetails)

    @Singleton
    @Provides
    fun updateAdvanceLevelInfoUseCase(appRepository: IUserRepository) = UpdateAdvanceLevelInfoUseCase(appRepository::updateAdvanceLevelInfo)

    @Singleton
    @Provides
    fun getPagedOffersUseCase(appRepository: IOffersRepository) = GetPagedOffersUseCase(appRepository::getPagedOffers)

    @Singleton
    @Provides
    fun getPagedMeetingsUseCase(appRepository: IMeetingsRepository) = GetPagedMeetingsUseCase(appRepository::getPagedMeetings)

    @Singleton
    @Provides
    fun getPagedPlayersUseCase(appRepository: IPlayersRepository) = GetPagedPlayersUseCase(appRepository::getPagedPlayers)

    @Singleton
    @Provides
    fun refreshMeetingsUseCase(meetingsStore: Store<MeetingsFilter, List<Meeting>>) = RefreshMeetingsUseCase {
        try {
            meetingsStore.fresh(it)
            Resource.Success(Unit)
        }
        catch (e: Exception) {
            Resource.Error(message = UiText.NonTranslatable(e.message?:e.toString()))
        }
    }

    @Singleton
    @Provides
    fun refreshOffersUseCase(offersStore: Store<OffersFilter, List<GameOffer>>) = RefreshOffersUseCase {
        try {
            offersStore.fresh(it)
            Resource.Success(Unit)
        }
        catch (e: Exception) {
            Resource.Error(message = UiText.NonTranslatable(e.message?:e.toString()))
        }
    }

    @Singleton
    @Provides
    fun refreshOffersToAcceptUseCase(offersToAcceptStore: Store<OffersFilter, List<GameOfferToAccept>>) = RefreshOffersToAcceptUseCase {
        try {
            offersToAcceptStore.fresh(it)
            Resource.Success(Unit)
        }
        catch (e: Exception) {
            Resource.Error(message = UiText.NonTranslatable(e.message?:e.toString()))
        }
    }

    @Singleton
    @Provides
    fun refreshPlayersUseCase(playersStore: Store<PlayersFilter, List<Player>>) = RefreshPlayersUseCase {
        try {
            playersStore.fresh(it)
            Resource.Success(Unit)
        }
        catch (e: Exception) {
            Resource.Error(message = UiText.NonTranslatable(e.message?:e.toString()))
        }
    }

    @Singleton
    @Provides
    fun deleteMyOfferToAcceptUseCase(appRepository: IResponsesRepository) = DeleteMyOfferToAcceptUseCase(appRepository::deleteMyOfferToAccept)

    @Singleton
    @Provides
    fun getPagedNotificationsUseCase(appRepository: INotificationsRepository) = GetPagedNotificationsUseCase(appRepository::getPagedNotifications)

    @Singleton
    @Provides
    fun rejectOfferToAcceptUseCase(appRepository: IResponsesRepository) = RejectOfferToAcceptUseCase(appRepository::rejectOfferToAccept)
}