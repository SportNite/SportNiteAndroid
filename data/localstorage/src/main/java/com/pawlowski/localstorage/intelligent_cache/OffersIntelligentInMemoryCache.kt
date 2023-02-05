package com.pawlowski.localstorage.intelligent_cache

import com.pawlowski.localstorage.intelligent_cache.base.ElementIdAndKeyExtractor
import com.pawlowski.localstorage.intelligent_cache.base.IntelligentMemoryCache
import com.pawlowski.models.GameOffer
import com.pawlowski.models.params_models.OffersFilter

class OffersIntelligentInMemoryCache: IntelligentMemoryCache<GameOffer, OffersFilter>(
    idExtractor = object : ElementIdAndKeyExtractor<GameOffer, OffersFilter>() {
        override fun extractId(element: GameOffer): Any {
            return element.offerUid
        }

        override fun doesElementBelongToKey(element: GameOffer, key: OffersFilter): Boolean {
            val sportIsSame = key.sportFilter?.let {
                it.sportId == element.sport.sportId
            }?:true
            return sportIsSame
        }
    }
)