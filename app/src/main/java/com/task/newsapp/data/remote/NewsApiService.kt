package com.task.newsapp.data.remote

import com.task.newsapp.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API Configuration to fetch news/details via Retrofit.
 */
interface NewsApiService {

    @GET("v2/top-headlines?")
    suspend fun getNews(
        @Query("country") country: String,
        @Query("page") page: Int,
        @Query("pageSize") itemsPerPage: Int) : NewsResponse
}