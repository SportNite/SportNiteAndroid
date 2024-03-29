package com.pawlowski.responses.data.cache

import com.pawlowski.cacheutils.intelligent_cache.base.ElementIdAndKeyExtractor
import com.pawlowski.cacheutils.intelligent_cache.base.IntelligentMemoryCache
import com.pawlowski.models.GameOfferToAccept
import com.pawlowski.models.params_models.OffersFilter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OffersToAcceptIntelligentInMemoryCache @Inject constructor(): IntelligentMemoryCache<GameOfferToAccept, OffersFilter>(
    idExtractor = object : ElementIdAndKeyExtractor<GameOfferToAccept, OffersFilter>() {
        override fun extractId(element: GameOfferToAccept): Any {
            return element.offerToAcceptUid
        }

        override fun doesElementBelongToKey(element: GameOfferToAccept, key: OffersFilter): Boolean {
            val sportIsSame = key.sportFilter?.let {
                it.sportId == element.offer.sport.sportId
            }?:true
            return sportIsSame
        }
    }
)