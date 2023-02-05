package com.pawlowski.network.di

import com.pawlowski.network.data.GraphQLService
import com.pawlowski.network.data.IGraphQLService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    internal fun graphqlService(graphQLService: GraphQLService): IGraphQLService = graphQLService
}