package com.pawlowski.choosecity.view_model_related

data class ChooseCityScreenUiState(
    val searchCityInput: String = "",
    val availableCities: List<String> = listOf(),
    val chosenCity: String? = null
)
