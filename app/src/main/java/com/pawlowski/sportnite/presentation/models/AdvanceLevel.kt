package com.pawlowski.sportnite.presentation.models

sealed class AdvanceLevel(asString: String) {
    data class NRTP(val nrtpLevel: Double): AdvanceLevel("${nrtpLevel.toString()} NRTP")
}
