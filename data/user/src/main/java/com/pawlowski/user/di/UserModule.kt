package com.pawlowski.user.di

import com.pawlowski.user.IUserRepository
import com.pawlowski.user.UserRepository
import com.pawlowski.user.use_cases.GetInfoAboutMeUseCase
import com.pawlowski.user.use_cases.GetUserSportsUseCase
import com.pawlowski.user.use_cases.SignOutUseCase
import com.pawlowski.user.use_cases.UpdateAdvanceLevelInfoUseCase
import com.pawlowski.user.use_cases.UpdateUserInfoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Singleton
    @Provides
    internal fun userRepository(userRepository: UserRepository): IUserRepository = userRepository

    @Singleton
    @Provides
    fun updateUserInfoUseCase(appRepository: IUserRepository): UpdateUserInfoUseCase = UpdateUserInfoUseCase(appRepository::updateUserInfo)

    @Singleton
    @Provides
    fun getInfoAboutMeUseCase(appRepository: IUserRepository): GetInfoAboutMeUseCase = GetInfoAboutMeUseCase(appRepository::getInfoAboutMe)

    @Singleton
    @Provides
    fun signOutUseCase(appRepository: IUserRepository): SignOutUseCase = SignOutUseCase(appRepository::signOut)

    @Singleton
    @Provides
    fun getUserSportsUseCase(appRepository: IUserRepository) = GetUserSportsUseCase(appRepository::getUserSports)

    @Singleton
    @Provides
    fun updateAdvanceLevelInfoUseCase(appRepository: IUserRepository) = UpdateAdvanceLevelInfoUseCase(appRepository::updateAdvanceLevelInfo)
}