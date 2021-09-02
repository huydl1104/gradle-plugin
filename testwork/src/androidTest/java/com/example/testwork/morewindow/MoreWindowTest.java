package com.example.testwork.morewindow;



import com.example.testwork.R;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


/**
 * @author yudongliang
 * create time 2021-09-01
 * describe : 多个window测试
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class MoreWindowTest {
    private SuggestActivity mActivity;

    @Rule
    public ActivityTestRule<SuggestActivity> mActivityRule = new ActivityTestRule<>(
            SuggestActivity.class);

    @Before
    public void initActivity() {
        mActivity = mActivityRule.getActivity();
    }

    @Test
    public void autoCompleteTextView_twoSuggestions() {
        // Type "So" to trigger two suggestions.
        onView(withId(R.id.auto_complete_text_view))
                .perform(typeText("So"), closeSoftKeyboard());

        // Check that both suggestions are displayed.
        onView(withText("South China Sea"))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(withText("Southern Ocean"))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void autoCompleteTextView_oneSuggestion() {
        // Type "South" to trigger one suggestion.
        onView(withId(R.id.auto_complete_text_view))
                .perform(typeTextIntoFocusedView("South "), closeSoftKeyboard());

        // Should be displayed
        onView(withText("South China Sea"))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        // Should not be displayed.
        onView(withText("Southern Ocean"))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .check(doesNotExist());
    }

    @Test
    public void autoCompleteTextView_clickAndCheck() {
        // Type text into the text view
        onView(withId(R.id.auto_complete_text_view))
                .perform(typeTextIntoFocusedView("South "), closeSoftKeyboard());

        // Tap on a suggestion.
        onView(withText("South China Sea"))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .perform(click());

        // By clicking on the auto complete term, the text should be filled in.
        onView(withId(R.id.auto_complete_text_view))
                .check(matches(withText("South China Sea")));
    }

    @Test
    public void autoCompleteTextView_onDataClickAndCheck() {
        // NB: The autocompletion box is implemented with a ListView, so the preferred way
        // to interact with it is onData(). We can use inRoot here too!
        onView(withId(R.id.auto_complete_text_view))
                .perform(typeText("S"), closeSoftKeyboard());

        // This is useful because some of the completions may not be part of the View Hierarchy
        // unless you scroll around the list.
        onData(allOf(instanceOf(String.class), is("Baltic Sea")))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .perform(click());

        // The text should be filled in.
        onView(withId(R.id.auto_complete_text_view))
                .check(matches(withText("Baltic Sea")));
    }

}
