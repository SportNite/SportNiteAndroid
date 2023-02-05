package com.pawlowski.models

data class Player(
    val uid: String,
    val name: String,
    val photoUrl: String,
    //val advanceLevel: AdvanceLevel,
    val age: Int,
    val phoneNumber: String
)
