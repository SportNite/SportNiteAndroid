package com.pawlowski.sportnite.presentation.models

import com.google.android.gms.maps.model.LatLng
import com.pawlowski.models.Sport

data class SportObject(
    val availableSports: List<Sport>,
    val position: LatLng,
    val objectName: String
    )
