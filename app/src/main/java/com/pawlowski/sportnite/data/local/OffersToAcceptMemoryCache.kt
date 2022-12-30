package com.pawlowski.sportnite.data.local

import com.pawlowski.sportnite.domain.models.OffersFilter
import com.pawlowski.sportnite.presentation.models.GameOfferToAccept
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OffersToAcceptMemoryCache @Inject constructor(): InMemoryDataCache<GameOfferToAccept, OffersFilter>()