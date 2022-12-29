package com.pawlowski.sportnite.data.local

import kotlinx.coroutines.flow.*

abstract class InMemoryDataCache<T, K> {
    private val data by lazy {
        MutableStateFlow(listOf<T>())
    }

    fun getData(): List<T> {
        return data.value
    }

    suspend fun addData(key: K, element: T) {
        data.update {
            it.toMutableList().apply {
                add(element)
            }.toList()
        }
    }

    fun observeData(key: K): Flow<List<T>> = data.filter {
        filter(key)
    }

    suspend fun deleteAllData() {
        data.value = listOf()
    }

    suspend fun deleteData(element: T) {
        data.update {
            it.toMutableList().apply {
                remove(element)
            }.toList()
        }
    }

    abstract fun filter(key: K): Boolean
}