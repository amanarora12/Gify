package com.amanarora.gify.randomgifs;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.amanarora.gify.Constants;
import com.amanarora.gify.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class GifsActivityTest {
    @Rule
    public ActivityTestRule<GifsActivity> activityRule =
            new ActivityTestRule<>(GifsActivity.class, true, false);

    @Test
    public void noVideoUrlIntentShowErrorMessageTest() {
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        onView(withId(R.id.error_screen))
                .check(matches(isDisplayed()));
        onView(withId(R.id.player_view))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void startVideoFromIntentExtraUrlTest() {
        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA_GIF_URL_KEY,
                "https://media0.giphy.com/media/xUOxf3SQJtd3Ix5i4o/200.mp4");
        activityRule.launchActivity(intent);
        onView(withId(R.id.error_screen))
                .check(matches(not(isDisplayed())));
        onView(withId(R.id.player_view))
                .check(matches(isDisplayed()));
    }
}