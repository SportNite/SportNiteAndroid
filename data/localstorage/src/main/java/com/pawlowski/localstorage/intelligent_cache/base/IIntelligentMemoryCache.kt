package com.pawlowski.localstorage.intelligent_cache.base

import kotlinx.coroutines.flow.Flow

/**
 * Cache of data where key can be calculated from the value
 */
interface IIntelligentMemoryCache<V, K> {
    fun upsertElement(element: V)
    fun upsertManyElements(elements: List<V>)
    fun observeElements(key: K): Flow<List<V>>
    fun deleteElement(element: V)
    fun clearAll()
}