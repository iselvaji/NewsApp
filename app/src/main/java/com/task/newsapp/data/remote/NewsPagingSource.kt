package com.task.newsapp.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.task.newsapp.model.Articles

class NewsPagingSource(private val dataSource: RemoteDataSource) : PagingSource<Int, Articles>() {

    private val countryCode = "us"

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Articles> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = dataSource.getNews(countryCode, nextPageNumber, params.loadSize)
            LoadResult.Page(
                data = response.articles,
                prevKey = if (nextPageNumber > 0) nextPageNumber - 1 else null,
                nextKey = if (nextPageNumber < response.totalResults) nextPageNumber + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Articles>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
