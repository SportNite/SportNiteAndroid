package com.pawlowski.sportnite.di

import com.pawlowski.network.data.IGraphQLService
import com.pawlowski.network.di.NetworkModule
import com.pawlowski.sportnite.fakes.FakeGraphQLService
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
object FakeNetworkModule {

    @Singleton
    @Provides
    fun graphqlService(): IGraphQLService = FakeGraphQLService
}