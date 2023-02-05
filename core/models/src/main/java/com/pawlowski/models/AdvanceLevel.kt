package com.pawlowski.models

sealed class AdvanceLevel(val asString: String) {
    data class NRTP(val nrtpLevel: Double): AdvanceLevel("$nrtpLevel NRTP")
    data class DefaultLevel(val level: Int): AdvanceLevel("$level/10")
}
