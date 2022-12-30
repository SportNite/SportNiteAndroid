package com.pawlowski.sportnite.data.local

import com.pawlowski.sportnite.domain.models.OffersFilter
import com.pawlowski.sportnite.presentation.models.GameOffer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OffersInMemoryCache @Inject constructor(): InMemoryDataCache<GameOffer, OffersFilter>()