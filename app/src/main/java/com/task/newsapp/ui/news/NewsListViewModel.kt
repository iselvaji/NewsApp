package com.task.newsapp.ui.news

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.task.newsapp.data.NetworkStatusRepository
import com.task.newsapp.data.NewsRepository
import com.task.newsapp.model.Articles
import com.task.newsapp.model.NetworkStatusState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
* ViewModel for the NewsListFragment screen.
* The ViewModel works with the [NewsRepository] to get the data from remote source
*/

@HiltViewModel
class NewsListViewModel @Inject constructor (
    private val networkStatusRepository: NetworkStatusRepository,
    private val repository: NewsRepository,
    application: Application
) : AndroidViewModel(application) {

    val networkState: StateFlow<NetworkStatusState> = networkStatusRepository.state

    fun isDeviceOnline() : Boolean = networkStatusRepository.hasNetworkConnection()

    fun fetchNews(): Flow<PagingData<Articles>> {
        return repository.getArticles().cachedIn(viewModelScope)
    }
}
