package com.task.newsapp

import com.task.newsapp.model.Articles
import com.task.newsapp.model.NewsResponse
import com.task.newsapp.model.Source

object TestData {

    fun getListOfArticles(): NewsResponse {
        return NewsResponse(
            status = "OK", totalResults= 2,
            articles = listOf(
                Articles(
                    source = Source(id = "CNN", name = "CNN"),
                    author = "Test Author",
                    title = "Test News",
                    description = "Test news description",
                    url = "https://www.cnn.com/europe/live-news/ukraine-russia-putin-news-04-21-22/index.html",
                    urlToImage = "https://cdn.cnn.com/cnnnext/dam/assets/220420195929-mariupol-azovstal-plant-0420-super-tease.jpg",
                    publishedAt = "2022-04-21T06:20:00Z",
                    content = "Test content"
                    ),
                Articles(
                    source = Source(id = "ESPN", name = "ESPN"),
                    author = "Test Author2",
                    title = "Test News2",
                    description = "Test news description2",
                    url = "https://www.espn.com/nba/recap/_/gameId/401430246",
                    urlToImage = "https://s.espncdn.com/stitcher/sports/basketball/nba/events/401430246.png?templateId=espn.com.share.1",
                    publishedAt = "2022-04-21T06:20:00Z",
                    content = "Test content2"
                )
            )
        )
    }

}