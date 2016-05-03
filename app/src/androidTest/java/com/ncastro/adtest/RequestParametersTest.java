package com.ncastro.adtest;

import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Nuno on 03/05/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RequestParametersTest {

    public static final String USER_ID_STRING = "JohnDoe";
    public static final String APP_ID_STRING = "2070";
    public static final String INVALID_APP_ID_STRING = "X2222";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void fillvalidrequest() {
        onView(withId(R.id.info_uid))
                .perform(typeText(USER_ID_STRING));
        onView(withId(R.id.scroll_container))
                .perform(swipeUp());
        onView(withId(R.id.info_appid))
                .perform(typeText(APP_ID_STRING), closeSoftKeyboard());

        onView(withId(R.id.button_proceed)).perform(click());

        onView(isRoot()).perform(waitId(R.id.offers_list, 2000));

        onView(withId(R.id.offers_list)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.error_container)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void fillinvalidrequest() {
        onView(withId(R.id.info_uid))
                .perform(typeText(USER_ID_STRING));
        onView(withId(R.id.scroll_container))
                .perform(swipeUp());
        onView(withId(R.id.info_appid))
                .perform(typeText(INVALID_APP_ID_STRING), closeSoftKeyboard());

        onView(withId(R.id.button_proceed)).perform(click());

        onView(isRoot()).perform(waitId(R.id.error_container, 2000));

        onView(withId(R.id.offers_list)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.error_container)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }


    /**
     * Perform action of waiting for a specific view id.
     */
    public static ViewAction waitId(final int viewId, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for a specific view with id <" + viewId + "> during " + millis + " millis.";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                final Matcher<View> viewMatcher = withId(viewId);

                do {
                    for (final View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return;
                        }
                    }

                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }


}
