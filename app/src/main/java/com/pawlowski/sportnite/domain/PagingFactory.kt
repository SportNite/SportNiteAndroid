package com.pawlowski.sportnite.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pawlowski.sportnite.utils.Resource
import javax.inject.Inject

private const val START_PAGE_INDEX = 1

class PagingFactory<T: Any> @Inject constructor(
    private val request: suspend (page: Int, pageSize: Int) -> Resource<List<T>>,
): PagingSource<Int, T>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val position = params.key ?: START_PAGE_INDEX

        return when(val result = request(position, params.loadSize)) {
            is Resource.Success -> {
                result.data?.let {
                    val nextKey = if(result.data.isEmpty())
                        null
                    else
                        position + (params.loadSize / 10)
                    LoadResult.Page(
                        data = result.data,
                        prevKey = if(position == START_PAGE_INDEX)
                            null
                        else
                            position - 1,
                        nextKey = nextKey
                    )
                }?:LoadResult.Error(Exception())

            }
            is Resource.Error -> {
                LoadResult.Error(Exception())
            }
        }



    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}