package com.example.testwork.basicSImple

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import android.app.Activity
import androidx.test.core.app.ActivityScenario
//import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.testwork.R
import com.example.testwork.basicSimiple.BasicSimpleActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
/**
 * @author yudongliang
 * create time 2021-09-01
 * describe :
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class ChangeTextBehaviorKtTest {

    /**
     * Use [ActivityScenarioRule] to create and launch the activity under test before each test,
     * and close it after each test. This is a replacement for
     * [androidx.test.rule.ActivityTestRule].
     */
//    @get:Rule
//    var activityScenarioRule = ActivityScenarioRule<BasicSimpleActivity>()

    @Test
    fun changeText_sameActivity() {

        // Type text and then press the button.
        Espresso.onView(withId(R.id.editTextUserInput))
                .perform(ViewActions.typeText(STRING_TO_BE_TYPED), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.changeTextBt)).perform(ViewActions.click())

        // Check that the text was changed.
        Espresso.onView(withId(R.id.textToBeChanged)).check(ViewAssertions.matches(ViewMatchers.withText(STRING_TO_BE_TYPED)))
    }

    @Test
    fun changeText_newActivity() {
        // Type text and then press the button.
        Espresso.onView(withId(R.id.editTextUserInput)).perform(ViewActions.typeText(STRING_TO_BE_TYPED),
                ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.activityChangeTextBtn)).perform(ViewActions.click())

        // This view is in a different Activity, no need to tell Espresso.
        Espresso.onView(withId(R.id.show_text_view)).check(ViewAssertions.matches(ViewMatchers.withText(STRING_TO_BE_TYPED)))
    }

    companion object {

        val STRING_TO_BE_TYPED = "ChangeTextBehaviorKtTest"
    }

}