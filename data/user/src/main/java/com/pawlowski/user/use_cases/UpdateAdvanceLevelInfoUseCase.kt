package com.pawlowski.user.use_cases

import com.pawlowski.models.AdvanceLevel
import com.pawlowski.models.Sport
import com.pawlowski.utils.Resource

fun interface UpdateAdvanceLevelInfoUseCase: suspend (Map<Sport, AdvanceLevel>) -> Resource<Unit>