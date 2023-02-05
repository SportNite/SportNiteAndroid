package com.pawlowski.localstorage.intelligent_cache.base

import kotlinx.coroutines.flow.*

open class IntelligentMemoryCache<V, K>(
    private val idExtractor: ElementIdAndKeyExtractor<V, K>
): IIntelligentMemoryCache<V, K> {
    private val _elementsFlow = MutableStateFlow(listOf<V>())

    override fun upsertElement(element: V) {
        _elementsFlow.update { lastValue ->
            val newElementId = idExtractor.extractId(element)
            lastValue.toMutableList().apply {
                removeIf {
                    idExtractor.extractId(it) == newElementId
                }
                add(element)
            }
        }
    }

    override fun upsertManyElements(elements: List<V>) {
        _elementsFlow.update { lastValue ->
            lastValue.toMutableList().apply {
                elements.forEach { element ->
                    val newElementId = idExtractor.extractId(element)
                    removeIf {
                        idExtractor.extractId(it) == newElementId
                    }
                    add(element)
                }

            }
        }
    }

    override fun observeElements(key: K): Flow<List<V>> {
        return _elementsFlow.map { elementsList ->
            elementsList.filter {
                idExtractor.doesElementBelongToKey(it, key)
            }
        }
    }

    override fun deleteElement(element: V) {
        _elementsFlow.update { lastValue ->
            val elementToDeleteId = idExtractor.extractId(element)
            lastValue.toMutableList().apply {
                removeIf {
                    idExtractor.extractId(it) == elementToDeleteId
                }
            }
        }
    }

    override fun clearAll() {
        _elementsFlow.value = listOf()
    }
}