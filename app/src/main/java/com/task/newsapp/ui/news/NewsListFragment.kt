package com.task.newsapp.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.task.newsapp.R
import com.task.newsapp.databinding.FragmentNewsListBinding
import com.task.newsapp.model.Articles
import com.task.newsapp.model.NetworkStatusState
import com.task.newsapp.ui.news.adapter.NewsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * News list fragment of the application, fetch and load the news articles from the remote
 */

@AndroidEntryPoint
class NewsListFragment : Fragment() {

    @Inject
    lateinit var listAdapter: NewsListAdapter

    private var binding: FragmentNewsListBinding? = null
    private val viewModel by viewModels<NewsListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObservers()
        fetchData()
    }

    private fun setupUI() {

        binding?.apply {
            viewPagerNews.orientation = ViewPager2.ORIENTATION_VERTICAL
            viewPagerNews.adapter = listAdapter

            listAdapter.onItemClick = { selectedItem, _ ->
                navigateToDetailsScreen(selectedItem)
            }
        }
    }

    private fun setupObservers() {
        viewModel.networkState.asLiveData().observe(viewLifecycleOwner) { state ->
            when(state) {
                NetworkStatusState.NetworkStatusConnected -> {
                    fetchData()
                }
                else -> {
                    context?.let {
                        showNetworkErrorUI()
                        showNoResultsUI()
                    }
                }
            }
        }

        // pagination load state UI
        listAdapter.loadStateFlow.asLiveData().observe(viewLifecycleOwner) { loadState ->

            binding?.apply {
                progressbar.isVisible = loadState.source.refresh is LoadState.Loading
                viewPagerNews.isVisible = loadState.source.refresh is LoadState.NotLoading

                // Ui for no results found
                if (loadState.source.refresh is LoadState.NotLoading
                    && loadState.append.endOfPaginationReached
                    && listAdapter.itemCount < 1
                ) {
                    showNoResultsUI()
                } else {
                    viewPagerNews.isVisible = true
                    textNoResults.isVisible = false
                }
            }
        }
    }

    // fetch the data to be displayed
    private fun fetchData() {
        if(!viewModel.isDeviceOnline()) {
            showNetworkErrorUI()
        } else {
            lifecycleScope.launch {
                viewModel.fetchNews().collectLatest {
                    listAdapter.submitData(it)
                }
            }
        }
    }

    // navigate to art details screen
    private fun navigateToDetailsScreen(selectedItem : Articles) {
        val directions = NewsListFragmentDirections.actionListToDetailsView(selectedItem)
        findNavController().navigate(directions)
    }

    // display the network unAvailable message in the UI
    private fun showNetworkErrorUI() {
        binding?.root?.let {
            Snackbar.make(it, getString(R.string.err_network),Snackbar.LENGTH_LONG).show()
        }
    }

    private fun showNoResultsUI() {
        binding?.apply {
            viewPagerNews.isVisible = false
            textNoResults.isVisible = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}