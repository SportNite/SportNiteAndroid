package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.models.params_models.MeetingsFilter
import com.pawlowski.utils.Resource

fun interface RefreshMeetingsUseCase : suspend (MeetingsFilter) -> Resource<Unit>