package com.pawlowski.cacheutils.key_based_cache

import kotlinx.coroutines.flow.*

abstract class InMemoryDataCache<T, K> : IInMemoryDataCache<T, K> {
    private val data by lazy {
        MutableStateFlow(mapOf<K, List<T>>())
    }


    override fun addElement(key: K, element: T) {
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

    override fun <V>addManyElements(key: K, elements: List<T>, elementsEqualityChecker: (T) -> V) {
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

    override fun observeData(key: K): Flow<List<T>> {
        return data.map { map ->
           map[key] ?: listOf()
        }
    }

    override fun <V: Comparable<V>>observeData(key: K, sortBy: ((T) -> V)): Flow<List<T>> {
        return data.map { map ->
            val result = map[key] ?: listOf()
            result.sortedBy { sortBy(it) }
        }
    }

    override fun observeFirstFromAnyKey(predicate: (T) -> Boolean): Flow<T> {
        return data.map { map ->
            map.flatMap { it.value }.first { predicate(it) }
        }
    }

    override fun deleteAllData() {
        data.value = mapOf()
    }

    override fun deleteElement(key: K, element: T) {
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

    override fun deleteElement(key: K, predicate: (T) -> Boolean) {
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

    override fun deleteElementFromAllKeys(predicate: (T) -> Boolean) {
        data.update { lastMapState ->
            lastMapState.toMutableMap().map {
                Pair(
                    first = it.key,
                    second = it.value.toMutableList().apply {
                        removeIf(predicate)
                    }
                )
            }.toMap()
        }
    }


    override fun deleteAllElementsWithKey(key: K) {
        data.update {
            it.toMutableMap().apply {
                remove(key)
            }
        }
    }

    override fun updateElements(elementAfterUpdate: (K, T) -> T) {
        data.update { prevMap ->
            prevMap.map {
                Pair(it.key,it.value.map { item ->
                    elementAfterUpdate(it.key, item)
                })
            }.toMap()
        }
    }

}