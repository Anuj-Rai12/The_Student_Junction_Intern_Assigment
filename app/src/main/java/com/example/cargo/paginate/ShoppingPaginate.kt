package com.example.cargo.paginate

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cargo.api.ShoppingApi
import com.example.cargo.utils.ExtraFile
import com.example.data.DataSealed

private const val STARTING_PAGE_INDEX = 1

class ShoppingPaginate(private val shoppingApi: ShoppingApi) :
    PagingSource<Int, DataSealed>() {
    override fun getRefreshKey(state: PagingState<Int, DataSealed>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataSealed> {
        return try {
            val pageIndex = params.key ?: STARTING_PAGE_INDEX
            val response = shoppingApi.getShoppingApi(limit = pageIndex)
            val nextKey = if (response.isEmpty())
                null
            else
                pageIndex + (params.loadSize / ExtraFile.Load_size)
            val list: MutableList<DataSealed> = mutableListOf()
            if (pageIndex== STARTING_PAGE_INDEX) {
                list.add(
                    DataSealed.UserDescription(
                        title = "Hey Alex,",
                        description = "Find a course you want to learn"
                    )
                )
                list.add(DataSealed.Category(title = "Categories", textBtn = "See All"))
            }
            response.forEach {
                list.add(DataSealed.ShoppingInfo(it))
            }
            LoadResult.Page(
                data = list,
                prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}