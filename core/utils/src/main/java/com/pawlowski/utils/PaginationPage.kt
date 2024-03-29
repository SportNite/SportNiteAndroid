package com.pawlowski.utils

data class PaginationPage<T>(
    val hasNextPage: Boolean,
    val endCursor: String?,
    val data: List<T>
)