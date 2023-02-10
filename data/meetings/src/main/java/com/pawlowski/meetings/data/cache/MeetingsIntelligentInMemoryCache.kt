package com.pawlowski.meetings.data.cache

import com.pawlowski.cacheutils.intelligent_cache.base.ElementIdAndKeyExtractor
import com.pawlowski.cacheutils.intelligent_cache.base.IntelligentMemoryCache
import com.pawlowski.models.Meeting
import com.pawlowski.models.params_models.MeetingsFilter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MeetingsIntelligentInMemoryCache @Inject constructor(): IntelligentMemoryCache<Meeting, MeetingsFilter>(
    idExtractor = object : ElementIdAndKeyExtractor<Meeting, MeetingsFilter>() {
        override fun extractId(element: Meeting): Any {
            return element.meetingUid
        }

        override fun doesElementBelongToKey(element: Meeting, key: MeetingsFilter): Boolean {
            val sportIsSame = key.sportFilter?.let {
                element.sport.sportId == it.sportId
            }?:true

            return sportIsSame
        }
    }
)