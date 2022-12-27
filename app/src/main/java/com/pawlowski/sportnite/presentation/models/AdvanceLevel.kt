package com.pawlowski.sportnite.presentation.models

sealed class AdvanceLevel(val asString: String) {
    data class NRTP(val nrtpLevel: Double): AdvanceLevel("$nrtpLevel NRTP")
}
