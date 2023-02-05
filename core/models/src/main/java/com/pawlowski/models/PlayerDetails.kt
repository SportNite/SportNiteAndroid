package com.pawlowski.models

data class PlayerDetails(
    val playerName: String,
    val playerPhotoUrl: String,
    val advanceLevels: Map<Sport, AdvanceLevel>,
    val age: Int,
    val playerUid: String,
    val contact: List<String>,
    val timeAvailability: String
)
