package com.pawlowski.sportnite.data.local

import com.pawlowski.sportnite.domain.models.MeetingsFilter
import com.pawlowski.sportnite.presentation.models.Meeting
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeetingsInMemoryCache @Inject constructor(): InMemoryDataCache<Meeting, MeetingsFilter>()