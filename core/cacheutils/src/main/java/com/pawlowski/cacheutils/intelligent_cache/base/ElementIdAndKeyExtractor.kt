package com.pawlowski.cacheutils.intelligent_cache.base

abstract class ElementIdAndKeyExtractor<V, K> {
    abstract fun extractId(element: V): Any
    abstract fun doesElementBelongToKey(element: V, key: K): Boolean
}