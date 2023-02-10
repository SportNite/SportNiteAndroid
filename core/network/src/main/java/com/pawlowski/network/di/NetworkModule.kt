package com.pawlowski.network.di

import com.apollographql.apollo3.ApolloClient
import com.pawlowski.network.data.AuthorizationInterceptor
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
    fun apolloClient(authorizationInterceptor: AuthorizationInterceptor): ApolloClient = ApolloClient.Builder()
        .serverUrl(serverUrl = "https://projektinzynieria.bieszczadywysokie.pl/graphql/")
        .addHttpInterceptor(authorizationInterceptor)
        .build()

    @Singleton
    @Provides
    internal fun graphqlService(graphQLService: GraphQLService): IGraphQLService = graphQLService
}