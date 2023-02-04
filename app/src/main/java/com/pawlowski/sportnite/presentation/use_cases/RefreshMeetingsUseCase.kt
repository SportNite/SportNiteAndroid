package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.sportnite.domain.models.MeetingsFilter
import com.pawlowski.utils.Resource

fun interface RefreshMeetingsUseCase : suspend (MeetingsFilter) -> Resource<Unit>