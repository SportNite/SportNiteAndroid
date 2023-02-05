package com.pawlowski.localstorage.di

import com.pawlowski.localstorage.intelligent_cache.OffersIntelligentInMemoryCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalStorageModule {

    @Singleton
    @Named("my")
    @Provides
    fun offersIntelligentCacheOther(): OffersIntelligentInMemoryCache = OffersIntelligentInMemoryCache()

    @Singleton
    @Named("other")
    @Provides
    fun offersIntelligentCacheMine(): OffersIntelligentInMemoryCache = OffersIntelligentInMemoryCache()
}