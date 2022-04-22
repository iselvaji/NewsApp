package com.task.newsapp.ui.news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.task.newsapp.databinding.ListItemNewsBinding
import com.task.newsapp.model.Articles
import javax.inject.Inject

/**
 * Paging Adapter with for the list of news to display by [NewsViewHolder]
 */

class NewsListAdapter @Inject constructor() : PagingDataAdapter<Articles,
        NewsListAdapter.NewsViewHolder>(DiffCallBack) {

    var onItemClick: ((item: Articles, view: View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding  = ListItemNewsBinding.inflate(inflater, parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    object DiffCallBack : DiffUtil.ItemCallback<Articles>() {
        override fun areItemsTheSame(oldItem: Articles, newItem: Articles): Boolean {
            return oldItem.content == newItem.content
        }

        override fun areContentsTheSame(oldItem: Articles, newItem: Articles): Boolean {
            return oldItem == newItem
        }
    }

    inner class NewsViewHolder(private var binding: ListItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(data: Articles) {
            binding.apply {
                title.text = data.title
                details.text = data.description
                imgNews.load(data.urlToImage)
            }
        }

        override fun onClick(view: View?) {
            if (view != null) {
                getItem(absoluteAdapterPosition)?.let { onItemClick?.invoke(it, view) }
            }
        }
    }
}