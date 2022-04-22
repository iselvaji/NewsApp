package com.task.newsapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.task.newsapp.data.remote.NewsPagingSource
import com.task.newsapp.data.remote.RemoteDataSource
import com.task.newsapp.model.Articles
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository implementation that uses [androidx.paging.PagingSource]
 * to load pages from network when there are no more items cached
 */

@Singleton
class NewsRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) {

    @OptIn(ExperimentalPagingApi::class)
    fun getArticles(): Flow<PagingData<Articles>> {
        return Pager(
            config = PagingConfig(PAGE_SIZE, enablePlaceholders = true, prefetchDistance = PRE_FETCH_DISTANCE),
            pagingSourceFactory = { NewsPagingSource(remoteDataSource) }
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 10
        const val PRE_FETCH_DISTANCE = 2
    }
}