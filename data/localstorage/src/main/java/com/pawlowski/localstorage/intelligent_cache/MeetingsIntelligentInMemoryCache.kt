package com.pawlowski.localstorage.intelligent_cache

import com.pawlowski.localstorage.intelligent_cache.base.ElementIdAndKeyExtractor
import com.pawlowski.localstorage.intelligent_cache.base.IntelligentMemoryCache
import com.pawlowski.models.Meeting
import com.pawlowski.models.params_models.MeetingsFilter

class MeetingsIntelligentInMemoryCache: IntelligentMemoryCache<Meeting, MeetingsFilter>(
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