package com.task.newsapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.task.newsapp.data.remote.NewsPagingSource
import com.task.newsapp.data.remote.RemoteDataSource
import com.task.newsapp.model.Articles
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NewsPagingSourceTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var dataSource: RemoteDataSource

    private lateinit var pagingSource: NewsPagingSource

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        pagingSource = NewsPagingSource(dataSource)
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun test_paging_source_load_failure() = runBlockingTest {

        val error = HttpException(
            Response.error<ResponseBody>(403
                , "some content".toResponseBody("plain/text".toMediaTypeOrNull())
            ))

        Mockito.doThrow(error)
            .`when`(dataSource)
            .getNews(anyString(), anyInt(), anyInt())

        val expectedResult = PagingSource.LoadResult.Error<Int, Articles>(error)

        assertEquals(
            expectedResult, pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun test_paging_source_load_refresh_success() = runBlockingTest {

        val news = TestData.getListOfArticles()

        Mockito.doReturn(news)
            .`when`(dataSource)
            .getNews(anyString(),anyInt(),anyInt())

        val expectedResult = PagingSource.LoadResult.Page(
            data = news.articles,
            prevKey = 0,
            nextKey = 2
        )

        assertEquals(
            expectedResult, pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 30,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun test_paging_source_load_prepend_success()= runBlockingTest {

        val news = TestData.getListOfArticles()

        Mockito.doReturn(news)
            .`when`(dataSource)
            .getNews(anyString(),anyInt(),anyInt())

        val expectedResult = PagingSource.LoadResult.Page(
            data = news.articles,
            prevKey = 0,
            nextKey = 2
        )

        assertEquals(
            expectedResult, pagingSource.load(
                PagingSource.LoadParams.Prepend(
                    key = 1,
                    loadSize = 20,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun test_paging_source_load_append_success() = runBlockingTest {

        val news = TestData.getListOfArticles()

        Mockito.doReturn(news)
            .`when`(dataSource)
            .getNews(anyString(),anyInt(),anyInt())

        val expectedResult = PagingSource.LoadResult.Page(
            data = news.articles,
            prevKey = 1,
            nextKey = null
        )

        assertEquals(
            expectedResult, pagingSource.load(
                PagingSource.LoadParams.Append(
                    key = 2,
                    loadSize = 30,
                    placeholdersEnabled = false
                )
            )
        )
    }
}