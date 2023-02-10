package com.pawlowski.commonui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

fun <T: Any>LazyGridScope.displayPagingItemsWithIndicators(
    pagingItems: LazyPagingItems<T>?,
    gridColumnsNumber: Int = 2,
    itemContent: @Composable (T) -> Unit
) {
    item(span = { GridItemSpan(gridColumnsNumber) }) {
        if(pagingItems?.areNoResults() == true) {
            NoItemsFoundCard()
        }
    }
    item(span = { GridItemSpan(gridColumnsNumber) }) {
        when(pagingItems?.loadState?.refresh)
        {
            is LoadState.Loading -> {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .size(30.dp),
                    )
                }
            }
            is LoadState.Error -> {
                Button(onClick = { pagingItems.retry() }) {
                    Text(text = "Try again")
                }
            }
            else -> {
                //Do nothing
            }
        }
    }
    pagingItems?.let {
        items(pagingItems.itemCount) {
            pagingItems[it]?.let { it1 ->
                itemContent(it1)
            }
        }
    }

    item(span = { GridItemSpan(gridColumnsNumber) })
    {
        when(pagingItems?.loadState?.append)
        {
            is LoadState.Loading -> {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .size(30.dp),
                    )
                }
            }
            is LoadState.Error -> {
                Button(onClick = { pagingItems.retry() }) {
                    Text(text = "Try again")
                }
            }
            else -> {
                //Do nothing
            }
        }

    }
}

fun <T: Any>LazyListScope.displayPagingItemsWithIndicators(
    pagingItems: LazyPagingItems<T>?,
    itemContent: @Composable (T) -> Unit
) {
    item {
        if(pagingItems?.areNoResults() == true) {
            NoItemsFoundCard()
        }
    }
    item {
        when(pagingItems?.loadState?.refresh)
        {
            is LoadState.Loading -> {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .size(30.dp),
                    )
                }
            }
            is LoadState.Error -> {
                Button(onClick = { pagingItems.retry() }) {
                    Text(text = "Try again")
                }
            }
            else -> {
                //Do nothing
            }
        }
    }
    pagingItems?.let {
        items(pagingItems.itemCount) {
            pagingItems[it]?.let { it1 ->
                itemContent(it1)
            }
        }
    }

    item {
        when(pagingItems?.loadState?.append)
        {
            is LoadState.Loading -> {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .size(30.dp),
                    )
                }
            }
            is LoadState.Error -> {
                Button(onClick = { pagingItems.retry() }) {
                    Text(text = "Try again")
                }
            }
            else -> {
                //Do nothing
            }
        }

    }
}

fun <T: Any> LazyPagingItems<T>.areNoResults(): Boolean
{
    return this.itemCount == 0 &&
            this.loadState.refresh is LoadState.NotLoading &&
            this.loadState.append is LoadState.NotLoading
}