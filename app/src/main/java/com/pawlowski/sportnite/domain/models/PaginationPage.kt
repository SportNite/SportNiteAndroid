package com.pawlowski.sportnite.domain.models

data class PaginationPage<T>(
    val hasNextPage: Boolean,
    val endCursor: String?,
    val data: List<T>
)