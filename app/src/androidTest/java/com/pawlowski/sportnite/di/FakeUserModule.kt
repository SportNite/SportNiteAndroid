package com.pawlowski.sportnite.di

import com.pawlowski.sportnite.fakes.FakeUserRepository
import com.pawlowski.user.IUserRepository
import com.pawlowski.user.di.UserModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [UserModule::class]
)
object FakeUserModule {

    @Singleton
    @Provides
    internal fun userRepository(): IUserRepository = FakeUserRepository
}