package com.pawlowski.sportnite.domain

import androidx.compose.runtime.mutableStateMapOf
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pawlowski.sportnite.domain.models.PaginationPage
import com.pawlowski.sportnite.utils.Resource
import javax.inject.Inject


class PagingFactory<T: Any> @Inject constructor(
    private val request: suspend (page: String?, pageSize: Int) -> Resource<PaginationPage<T>>,
): PagingSource<String, T>() {
    private val previousKeysMap = mutableStateMapOf<String, String?>()
    private val nextKeysMap = mutableStateMapOf<String, String?>()

    override suspend fun load(params: LoadParams<String>): LoadResult<String, T> {
        val position = params.key
        return when(val result = request(position, params.loadSize)) {
            is Resource.Success -> {
                result.data?.let {
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
                }?:LoadResult.Error(Exception())

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