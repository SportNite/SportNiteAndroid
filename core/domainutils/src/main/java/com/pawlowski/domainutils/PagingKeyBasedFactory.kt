package com.pawlowski.domainutils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pawlowski.utils.PaginationPage
import com.pawlowski.utils.Resource

class PagingKeyBasedFactory<T: Any> constructor(
    private val request: suspend (page: String?, pageSize: Int) -> Resource<PaginationPage<T>>,
): PagingSource<String, T>() {
    private val previousKeysMap = mutableMapOf<String, String?>()
    private val nextKeysMap = mutableMapOf<String, String?>()

    override suspend fun load(params: LoadParams<String>): LoadResult<String, T> {
        val position = params.key
        return when(val result = request(position, params.loadSize)) {
            is Resource.Success -> {
                result.data.let {
                    val nextKey = it.endCursor

                    nextKey?.let {
                        previousKeysMap[nextKey] = position
                        position?.let {
                            nextKeysMap[position] = nextKey
                        }
                    }

                    LoadResult.Page(
                        data = it.data,
                        prevKey = previousKeysMap[position],
                        nextKey = nextKey
                    )
                }

            }
            is Resource.Error -> {
                LoadResult.Error(Exception())
            }
        }



    }

    override fun getRefreshKey(state: PagingState<String, T>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            nextKeysMap[state.closestPageToPosition(anchorPosition)?.prevKey]
                ?:previousKeysMap[state.closestPageToPosition(anchorPosition)?.nextKey]

//            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
//                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}