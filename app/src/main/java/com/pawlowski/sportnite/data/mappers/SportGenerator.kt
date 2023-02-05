package com.pawlowski.sportnite.data.mappers

import com.pawlowski.sportnite.R
import com.pawlowski.models.AdvanceLevel
import com.pawlowski.models.Sport
import com.pawlowski.utils.UiText

fun getSportFromSportId(sportId: String): Sport {
    return availableSports[sportId]?: run {
        Sport(
            sportName = UiText.NonTranslatable(sportId),
            sportBackgroundUrl = "https://images.unsplash.com/photo-1480264104733-84fb0b925be3?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2670&q=80",
            sportId = sportId,
            sportIconId = R.drawable.sport_icon
        )
    }
}

fun getAvailableLevelsForSport(sport: Sport): List<AdvanceLevel> {
    return when(sport.sportId) {
        "TENNIS" -> {
            (1..11).map {
                AdvanceLevel.NRTP(nrtpLevel = it*0.5)
            }
        }
        else -> {
            (1..10).map {
                AdvanceLevel.DefaultLevel(level = it)
            }
        }

    }
}

val availableSports by lazy {
    listOf(
        Sport(
            sportName = UiText.NonTranslatable("Tenis ziemny"),
            sportBackgroundUrl = "https://images.unsplash.com/photo-1499510318569-1a3d67dc3976?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1364&q=80",
            sportId = "TENNIS",
            sportIconId = R.drawable.tennis_icon
        ),
        Sport(
            sportName = UiText.NonTranslatable("Bieganie"),
            sportBackgroundUrl = "https://images.unsplash.com/photo-1461896836934-ffe607ba8211?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2670&q=80",
            sportId = "RUN",
            sportIconId = R.drawable.run_icon
        ),
    ).groupBy { it.sportId }.mapValues { it.value[0] }
}