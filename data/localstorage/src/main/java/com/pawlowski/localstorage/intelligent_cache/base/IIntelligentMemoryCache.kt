package com.pawlowski.localstorage.intelligent_cache.base

import kotlinx.coroutines.flow.Flow

/**
 * Cache of data where key can be calculated from the value
 */
interface IIntelligentMemoryCache<V, K> {
    fun upsertElement(element: V)
    fun upsertManyElements(elements: List<V>)

    fun updateElementsIf(predicate: (V) -> Boolean, newValue: (V) -> V)
    fun <T: Comparable<T>>observeData(key: K?, sortBy: ((V) -> T?)): Flow<List<V>>
    fun observeData(key: K?): Flow<List<V>>
    fun deleteElement(element: V)
    fun deleteElementsIf(predicate: (V) -> Boolean)
    fun deleteAllElementsWithKey(key: K)
    fun clearAll()
}