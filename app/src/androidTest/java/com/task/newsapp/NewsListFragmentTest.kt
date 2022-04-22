package com.task.newsapp

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.task.newsapp.ui.news.NewsListFragment
import com.task.newsapp.util.launchFragmentInHiltContainer
import com.task.newsapp.util.waitUntilVisibleAction
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock


/**
 * Test class which contains the test cases for viewpager news list UI actions
 *
 */

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NewsListFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val mockNavController = mock(NavController::class.java)

    @Before
    fun setup() {
        hiltRule.inject()

        launchFragmentInHiltContainer<NewsListFragment> {
            mockNavController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(requireView(), mockNavController)
        }
    }

    @Test
    fun test_dataFetchSuccessUI() {

        onView(withId(R.id.viewPagerNews)).perform(waitUntilVisibleAction())
        onView(withId(R.id.viewPagerNews)).check(matches(isDisplayed()))
    }

    @Test
    fun test_dataFetchFailedUI() {

        // switch off the wifi/data network
        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc wifi disable")
        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc data disable")

        onView(withId(R.id.textNoResults)).perform(waitUntilVisibleAction())
        onView(withId(R.id.textNoResults)).check(matches(isDisplayed()))

        // switch on the wifi/data network
        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc wifi enable")
        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc data enable")
    }


    @Test
    fun test_navigateToDetailFragment(){

        onView(withId(R.id.viewPagerNews)).perform(waitUntilVisibleAction())
        onView(withId(R.id.viewPagerNews)).check(matches(isDisplayed()))

        onView(withId(R.id.viewPagerNews)).perform(swipeLeft())
        mockNavController.currentDestination?.id?.let { assert(it == R.id.details_fragment) }
    }

}
