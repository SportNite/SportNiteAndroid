package com.pawlowski.sportnite.presentation.use_cases

import com.pawlowski.sportnite.presentation.models.AdvanceLevel
import com.pawlowski.sportnite.presentation.models.Sport
import com.pawlowski.sportnite.utils.Resource

fun interface UpdateAdvanceLevelInfoUseCase: suspend (Map<Sport, AdvanceLevel>) -> Resource<Unit>