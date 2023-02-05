package com.pawlowski.sportnite.data.local

import com.pawlowski.models.params_models.OffersFilter
import com.pawlowski.models.GameOfferToAccept
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OffersToAcceptMemoryCache @Inject constructor(): InMemoryDataCache<GameOfferToAccept, OffersFilter>()