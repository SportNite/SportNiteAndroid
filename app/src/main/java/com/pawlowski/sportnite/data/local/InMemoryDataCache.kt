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
                }?: kotlin.run {
                    put(key, listOf(element))
                }
            }


        }
    }

    fun addManyElements(key: K, elements: List<T>) {
        data.update {
            it.toMutableMap().apply {
                get(key)?.toMutableList()?.addAll(elements) ?: kotlin.run {
                    put(key, elements)
                }
            }


        }
    }

    fun observeData(key: K): Flow<List<T>> {
        return data.map {
            it[key] ?: listOf()
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

    fun deleteAllElementsWithKey(key: K) {
        data.update {
            it.toMutableMap().apply {
                remove(key)
            }
        }
    }

}