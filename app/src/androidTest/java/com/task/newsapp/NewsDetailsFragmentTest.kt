package com.task.newsapp

import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.task.newsapp.ui.details.NewsDetailsFragment
import com.task.newsapp.util.launchFragmentInHiltContainer
import com.task.newsapp.util.waitUntilVisibleAction
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

/**
 * Test class which contains the test cases for news details UI
 *
 */

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
class NewsDetailsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun test_details_fragment_success() {

        val article = TestDatas.getArticles()
        val args = bundleOf("article" to article)

        launchFragmentInHiltContainer<NewsDetailsFragment>(
            themeResId = R.style.Theme_NewsApp,
            fragmentArgs = args
        )

        onView(withId(R.id.webView)).perform(waitUntilVisibleAction())

        onView(withId(R.id.textNoResults))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(R.id.webView)).check(matches(isDisplayed()))
    }

    @Test
    fun test_details_fragment_no_data() {

        val article = TestDatas.getArticleWithEmptyLink()
        val args = bundleOf("article" to article)

        launchFragmentInHiltContainer<NewsDetailsFragment>(
            themeResId = R.style.Theme_NewsApp,
            fragmentArgs = args
        )
        onView(withId(R.id.textNoResults)).perform(waitUntilVisibleAction())
        onView(withId(R.id.textNoResults)).check(matches(isDisplayed()))
    }
}