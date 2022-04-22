package com.task.newsapp

import com.task.newsapp.model.Articles
import com.task.newsapp.model.Source

object TestDatas {

    fun getArticles(): Articles {
        return Articles(
            source = Source(id = "CNN", name = "CNN"),
            author = "Test Author",
            title = "Test News",
            description = "Test news description",
            url = "https://www.cnn.com/europe/live-news/ukraine-russia-putin-news-04-21-22/index.html",
            urlToImage = "https://cdn.cnn.com/cnnnext/dam/assets/220420195929-mariupol-azovstal-plant-0420-super-tease.jpg",
            publishedAt = "2022-04-21T06:20:00Z",
            content = "Test content"
        )
    }

    fun getArticleWithEmptyLink(): Articles {
        return Articles(
            source = Source(id = "CNN", name = "CNN"),
            author = "Test Author",
            title = "Test News",
            description = "Test news description",
            url = null,
            urlToImage = "https://cdn.cnn.com/cnnnext/dam/assets/220420195929-mariupol-azovstal-plant-0420-super-tease.jpg",
            publishedAt = "2022-04-21T06:20:00Z",
            content = "Test content"
        )
    }
}