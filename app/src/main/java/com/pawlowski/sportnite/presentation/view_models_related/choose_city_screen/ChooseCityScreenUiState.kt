package com.pawlowski.sportnite.presentation.view_models_related.choose_city_screen

data class ChooseCityScreenUiState(
    val searchCityInput: String = "",
    val availableCities: List<String> = listOf(),
    val chosenCity: String? = null
)
