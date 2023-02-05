package com.pawlowski.sportnite.data.local

import com.pawlowski.models.params_models.MeetingsFilter
import com.pawlowski.models.Meeting
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeetingsInMemoryCache @Inject constructor(): InMemoryDataCache<Meeting, MeetingsFilter>()