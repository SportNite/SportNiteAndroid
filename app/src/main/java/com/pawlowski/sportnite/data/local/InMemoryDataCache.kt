package com.pawlowski.sportnite.data.local

import kotlinx.coroutines.flow.*

abstract class InMemoryDataCache<T, K> {
    private val data by lazy {
        MutableStateFlow(mapOf<K, List<T>>())
    }

//    fun getData(key: K): List<T> {
//        return data.value[key]?: listOf()
//    }

    fun addElement(key: K, element: T) {
        data.update {
            it.toMutableMap().apply {
                get(key)?.let { currentList ->
                    replace(key, currentList.toMutableList().apply {
                        add(element)
                    })
                } ?: kotlin.run {
                    put(key, listOf(element))
                }
            }


        }
    }

    fun <V>addManyElements(key: K, elements: List<T>, elementsEqualityChecker: (T) -> V) {
        data.update { lastStateValue ->
            lastStateValue.toMutableMap().let { map ->
                map[key] = map[key]?.toMutableList()?.apply {
                    addAll(elements)
                }?.distinctBy(elementsEqualityChecker) ?: kotlin.run {
                    elements
                }
                map
            }
        }
    }

    fun observeData(key: K): Flow<List<T>> {
        return data.map { map ->
           map[key] ?: listOf()
        }
    }

    fun <V: Comparable<V>>observeData(key: K, sortBy: ((T) -> V)): Flow<List<T>> {
        return data.map { map ->
            val result = map[key] ?: listOf()
            result.sortedBy { sortBy(it) }
        }
    }

    fun observeFirstFromAnyKey(predicate: (T) -> Boolean): Flow<T> {
        return data.map {
            it.flatMap { it.value }.first { predicate(it) }
        }
    }

    fun deleteAllData() {
        data.value = mapOf()
    }

    fun deleteElement(key: K, element: T) {
        data.update {
            it.toMutableMap().apply {
                val currentList = get(key)
                currentList?.let {
                    replace(key, currentList.toMutableList().apply {
                        remove(element)
                    })
                }
            }
        }
    }

    fun deleteElement(key: K, predicate: (T) -> Boolean) {
        data.update {
            it.toMutableMap().apply {
                val currentList = get(key)
                currentList?.let {
                    replace(key, currentList.toMutableList().apply {
                        removeIf(predicate)
                    })
                }
            }
        }
    }

    fun deleteElementFromAllKeys(predicate: (T) -> Boolean) {
        data.update { lastMapState ->
            lastMapState.toMutableMap().apply {
                mapValues {
                    it.value.toMutableList().apply {
                        removeIf(predicate)
                    }
                }

            }
        }
    }


    fun deleteAllElementsWithKey(key: K) {
        data.update {
            it.toMutableMap().apply {
                remove(key)
            }
        }
    }

    fun updateElements(elementAfterUpdate: (K, T) -> T) {
        data.update { prevMap ->
            prevMap.map {
                Pair(it.key,it.value.map { item ->
                    elementAfterUpdate(it.key, item)
                })
            }.toMap()
        }
    }

}