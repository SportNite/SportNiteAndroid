package com.pawlowski.responses.use_cases

import com.pawlowski.utils.Resource

fun interface SendGameOfferToAcceptUseCase: suspend (String) -> Resource<String>