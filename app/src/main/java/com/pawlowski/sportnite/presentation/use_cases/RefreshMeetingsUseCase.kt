package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.sportnite.domain.models.MeetingsFilter

fun interface RefreshMeetingsUseCase : suspend (MeetingsFilter) -> Unit