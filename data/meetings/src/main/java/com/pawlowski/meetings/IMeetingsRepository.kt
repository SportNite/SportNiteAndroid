package com.pawlowski.meetings

import androidx.paging.PagingData
import com.pawlowski.models.Meeting
import com.pawlowski.models.Sport
import com.pawlowski.utils.UiData
import kotlinx.coroutines.flow.Flow

interface IMeetingsRepository {
    fun getMeetingDetails(meetingUid: String): Flow<UiData<Meeting>>
    fun getPagedMeetings(): Flow<PagingData<Meeting>>
    fun getIncomingMeetings(sportFilter: Sport? = null): Flow<UiData<List<Meeting>>>
}