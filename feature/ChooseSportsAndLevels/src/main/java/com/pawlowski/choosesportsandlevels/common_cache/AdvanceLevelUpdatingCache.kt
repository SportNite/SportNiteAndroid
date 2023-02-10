package com.pawlowski.choosesportsandlevels.common_cache

import com.pawlowski.models.AdvanceLevel
import com.pawlowski.models.Sport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AdvanceLevelUpdatingCache @Inject constructor() {
    private val _chosenSports: MutableStateFlow<Map<Sport, AdvanceLevel?>> by lazy {
        MutableStateFlow(mapOf())
    }
    val chosenSports get() = _chosenSports.asStateFlow()

    fun setSportsAndClearLevels(sports: List<Sport>) {
        _chosenSports.update {
            sports.associateWith { null }
        }
    }

    fun setAdvanceLevelOfSport(sport: Sport, advanceLevel: AdvanceLevel) {
        _chosenSports.update {
            it.toMutableMap().apply {
                replace(sport, advanceLevel)
            }
        }
    }

}