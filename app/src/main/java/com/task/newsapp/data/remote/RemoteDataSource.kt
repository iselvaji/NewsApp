package com.task.newsapp.data.remote

import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: NewsApiService) {

    suspend fun getNews(countryCode:String, position:Int, itemsPerPage:Int) = apiService.getNews(countryCode, position, itemsPerPage)
}