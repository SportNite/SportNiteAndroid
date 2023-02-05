package com.pawlowski.localstorage.key_based_cache

import com.pawlowski.models.params_models.OffersFilter
import com.pawlowski.models.GameOffer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OffersInMemoryCache @Inject constructor(): InMemoryDataCache<GameOffer, OffersFilter>()