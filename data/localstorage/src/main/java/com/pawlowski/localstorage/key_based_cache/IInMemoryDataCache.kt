package com.pawlowski.localstorage.key_based_cache

import kotlinx.coroutines.flow.Flow

interface IInMemoryDataCache<T, K> {
    fun addElement(key: K, element: T)
    fun <V> addManyElements(key: K, elements: List<T>, elementsEqualityChecker: (T) -> V)
    fun observeData(key: K): Flow<List<T>>
    fun <V : Comparable<V>> observeData(key: K, sortBy: (T) -> V): Flow<List<T>>
    fun observeFirstFromAnyKey(predicate: (T) -> Boolean): Flow<T>
    fun deleteAllData()
    fun deleteElement(key: K, element: T)
    fun deleteElement(key: K, predicate: (T) -> Boolean)
    fun deleteElementFromAllKeys(predicate: (T) -> Boolean)
    fun deleteAllElementsWithKey(key: K)
    fun updateElements(elementAfterUpdate: (K, T) -> T)
}