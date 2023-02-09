package com.pawlowski.meetings.di

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.pawlowski.auth.IAuthManager
import com.pawlowski.localstorage.intelligent_cache.MeetingsIntelligentInMemoryCache
import com.pawlowski.meetings.IMeetingsRepository
import com.pawlowski.meetings.MeetingsRepository
import com.pawlowski.models.Meeting
import com.pawlowski.models.params_models.MeetingsFilter
import com.pawlowski.network.data.IGraphQLService
import com.pawlowski.utils.dataOrNull
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.ZoneOffset
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MeetingsModule {

    @Singleton
    @Provides
    internal fun meetingsRepository(meetingsRepository: MeetingsRepository): IMeetingsRepository = meetingsRepository

    @Singleton
    @Provides
    fun meetingStore(
        graphQLService: IGraphQLService,
        meetingsInMemoryCache: MeetingsIntelligentInMemoryCache,
        authManager: IAuthManager
    ): Store<MeetingsFilter, List<Meeting>> {
        return StoreBuilder.from(fetcher = Fetcher.of { filters: MeetingsFilter ->
            val userUid = authManager.getCurrentUserUid()!!
            graphQLService.getIncomingMeetings(filters, userUid).dataOrNull()!!
        }, sourceOfTruth = SourceOfTruth.of(
            reader = { key: MeetingsFilter ->
                meetingsInMemoryCache.observeData(key, sortBy= {
                    it.date.offsetDateTimeDate.toLocalDateTime().toInstant(ZoneOffset.UTC)
                })
            },
            writer = { _: MeetingsFilter, input: List<Meeting> ->
                meetingsInMemoryCache.upsertManyElements(input)
            },
            delete = { key: MeetingsFilter ->
                meetingsInMemoryCache.deleteAllElementsWithKey(key)
            },
            deleteAll = { meetingsInMemoryCache.clearAll() }
        )).build()
    }
}